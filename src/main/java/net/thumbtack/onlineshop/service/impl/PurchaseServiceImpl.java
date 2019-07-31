package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.BuyProductToBasketRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.dto.responce.BuyProductToBasketResponse;
import net.thumbtack.onlineshop.dto.responce.InformarionAdoutClientsForAdminResponse;
import net.thumbtack.onlineshop.dto.responce.ProductResponse;
import net.thumbtack.onlineshop.dto.responce.PurchaseResponse;
import net.thumbtack.onlineshop.entity.Basket;
import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.entity.Product;
import net.thumbtack.onlineshop.entity.Purchase;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
import net.thumbtack.onlineshop.repository.BasketRepository;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.ProductRepository;
import net.thumbtack.onlineshop.repository.PurchaseRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.PurchaseService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final ProductRepository productRepository;
    private final SessionRepository sessionRepository;
    private final ClientRepository clientRepository;
    private final BasketRepository basketRepository;
    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseServiceImpl(ProductRepository productRepository, SessionRepository sessionRepository, ClientRepository clientRepository, BasketRepository basketRepository, PurchaseRepository purchaseRepository) {
        this.productRepository = productRepository;
        this.sessionRepository = sessionRepository;
        this.clientRepository = clientRepository;
        this.basketRepository = basketRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public BuyProductResponse buyProduct(String cookie, BuyProductRequest request) throws OnlineShopExceptionOld {
        Session session = sessionRepository.findByToken(cookie).get();
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        if (productRepository.existsById(request.getId())) {
            Product product = productRepository.findById(request.getId()).get();
            if (product.getCount() < request.getCount()) {
                throw new OnlineShopExceptionOld("Недостаточно товаров на складе");
            } else if (client.getCash() < request.getCount() * request.getPrice()) {
                throw new OnlineShopExceptionOld("У вас недостаточно средств на счете");
            } else if (!request.getName().equals(product.getName()) || !request.getPrice().equals(product.getPrice())) {
                throw new OnlineShopExceptionOld("Указанная цена или название продукта и его реальная стоимость или название не совпадают");
            }
            product.setCount(product.getCount() - request.getCount());
            productRepository.save(product);
            client.setCash(client.getCash() - request.getCount() * request.getPrice());
            clientRepository.save(client);
            Purchase purchase;
            Purchase.IdClientAndProduct idClientAndProduct = new Purchase.IdClientAndProduct(client, product);
            purchase = new Purchase(idClientAndProduct, request.getCount(), request.getPrice() * request.getCount());
            if (purchaseRepository.existsByClientAndProduct(client.getId(), product.getId())) {
                Purchase newPurchase = purchaseRepository.findByClientAndProduct(client.getId(), product.getId()).get();
                newPurchase.setAmount(purchase.getAmount() + newPurchase.getAmount());
                newPurchase.setTotalCost(purchase.getTotalCost() + newPurchase.getTotalCost());
                purchaseRepository.save(newPurchase);
            } else {
                purchaseRepository.save(purchase);
            }
            return new BuyProductResponse(request.getId(), request.getName(), request.getPrice(), request.getCount());
        }
        return null;
    }

    @Override
    public BuyProductToBasketResponse buyProductsToBasket(String cookie, List<BuyProductToBasketRequest> request) throws OnlineShopExceptionOld {
        List<BuyProductResponse> listProductInBasket = new BasketServiceImpl(basketRepository, productRepository,
                sessionRepository, clientRepository).getBasket(cookie);
        Map<Integer, BuyProductResponse> mapProductInBasket = listProductInBasket.stream()
                .collect(Collectors.toMap(BuyProductResponse::getId, Function.identity()));
        Map<Integer, BuyProductToBasketRequest> mapRequest = request.stream()
                .collect(Collectors.toMap(BuyProductToBasketRequest::getId, Function.identity()));
        BuyProductToBasketRequest productRequest;
        BuyProductResponse productInBasket;
        List<BuyProductToBasketRequest> removeProductRequest = new ArrayList<>();
        int maxCost = 0;
        for (Map.Entry<Integer, BuyProductToBasketRequest> productRequestEntry : mapRequest.entrySet()) {
            productRequest = productRequestEntry.getValue();
            if (!mapProductInBasket.containsKey(productRequest.getId())) {
                removeProductRequest.add(productRequest);
                continue;
            }
            productInBasket = mapProductInBasket.get(productRequest.getId());
            if (!productRequest.getName().equals(productInBasket.getName()) ||
                    !productRequest.getPrice().equals(productInBasket.getPrice())) {
                removeProductRequest.add(productRequest);
                continue;
            }
            if (productRequest.getCount() == null) {
                productRequest.setCount(mapProductInBasket.get(productRequest.getId()).getCount());
            }
            if (productRequest.getCount() > productInBasket.getCount()) {
                productRequest.setCount(productInBasket.getCount());
            }
            maxCost = maxCost + (productRequest.getPrice() * productRequest.getCount());
        }
        for (BuyProductToBasketRequest buyProductToBasketRequest : removeProductRequest) {
            mapRequest.remove(buyProductToBasketRequest.getId());
        }
        Session session = sessionRepository.findByToken(cookie).get();
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        if (maxCost > client.getCash()) {
            throw new OnlineShopExceptionOld("У вас недостаточно денег на счете");
        }
        Iterable<Product> products = productRepository.findAllById(mapRequest.keySet());
        List<BuyProductResponse> boughtProductResponse = new ArrayList<>();
        List<BuyProductResponse> remainingProductResponse = new ArrayList<>();
        List<Basket> baskets = new ArrayList<>();
        List<Basket> basketsRemove = new ArrayList<>();
        List<Purchase> purchases = new ArrayList<>();
        for (Product product : products) {
            productRequest = mapRequest.get(product.getId());
            productInBasket = mapProductInBasket.get(product.getId());
            if (product.getCount() >= productRequest.getCount()) {
                product.setCount(product.getCount() - productRequest.getCount());
                productInBasket.setCount(productInBasket.getCount() - productRequest.getCount());
                Basket.IdClientAndProduct idClientAndProductForBasket = new Basket.IdClientAndProduct(client, product);
                Purchase.IdClientAndProduct idClientAndProductForPurchase = new Purchase.IdClientAndProduct(client, product);
                if (productInBasket.getCount() != 0) {
                    baskets.add(new Basket(idClientAndProductForBasket, productInBasket.getCount()));
                } else {
                    mapProductInBasket.remove(productInBasket.getId());
                    basketsRemove.add(new Basket(idClientAndProductForBasket, productInBasket.getCount()));
                    purchases.add(new Purchase(idClientAndProductForPurchase, productInBasket.getCount(),
                            productInBasket.getCount() * productInBasket.getPrice()));
                }
                boughtProductResponse.add(new BuyProductResponse(productRequest.getId(), productRequest.getName(),
                        productRequest.getPrice(), productRequest.getCount()));
            }
        }
        for (Map.Entry<Integer, BuyProductResponse> productInBasketEntry : mapProductInBasket.entrySet()) {
            productInBasket = productInBasketEntry.getValue();
            remainingProductResponse.add(new BuyProductResponse(productInBasket.getId(), productInBasket.getName(),
                    productInBasket.getPrice(), productInBasket.getCount()));
        }

        purchaseRepository.saveAll(purchases);
        productRepository.saveAll(products);
        basketRepository.deleteAll(basketsRemove);
        basketRepository.saveAll(baskets);
        BuyProductToBasketResponse response = new BuyProductToBasketResponse();
        response.setBought(boughtProductResponse);
        response.setRemaining(remainingProductResponse);
        return response;
    }

    @Override
    public ResponseEntity<?> getSummaryList(String cookie, List<Integer> idCategories, List<Integer> idProducts,
                                            List<Integer> idClients, Integer offset, Integer limit) {

        if (offset == null) {
            offset = 0;
        }
        if (limit == null) {
            limit = 0;
        }
        List<Purchase> purchases = null;
        if (idCategories == null && idProducts == null && idClients == null && limit == 0) {
            purchases = purchaseRepository.findAll(PageRequest.of(offset, (int) purchaseRepository.count())).getContent();
        } else if (idCategories == null && idProducts == null && idClients == null && limit > 0) {
            purchases = purchaseRepository.findAll(PageRequest.of(offset, limit)).getContent(); //all null

        } else if (idCategories != null && idCategories.isEmpty() && idProducts == null && idClients == null && limit == 0) {
            purchases = purchaseRepository.findAllByCategoryIsNull(PageRequest.of(offset, (int) purchaseRepository.count())).getContent();
        } else if (idCategories != null && idCategories.isEmpty() && idProducts == null && idClients == null && limit > 0) {
            purchases = purchaseRepository.findAllByCategoryIsNull(PageRequest.of(offset, limit)).getContent(); //all null category empty

        } else if (idCategories != null && !idCategories.isEmpty() && idProducts == null && idClients == null) {
            purchases = IterableUtils.toList(purchaseRepository.findAllByCategory(idCategories)); //all null with category

        } else if (idCategories == null && idProducts != null && !idProducts.isEmpty() && idClients == null) {
            purchases = IterableUtils.toList(purchaseRepository.findAllByIdProduct(idProducts)); //all null with product

        } else if (idCategories == null && idProducts == null && idClients != null && !idClients.isEmpty()) {
            purchases = IterableUtils.toList(purchaseRepository.findAllByIdClient(idClients)); //all null with client

        } else if (idCategories != null && idCategories.isEmpty() && idProducts != null && !idProducts.isEmpty() && idClients == null) {
            purchases = IterableUtils.toList(purchaseRepository.findAllByIdProductAndCategoryIsNull(idProducts)); //category is empty, with product совершенно бесполезная фича

        } else if (idCategories != null && idCategories.isEmpty() && idProducts == null && idClients != null && !idClients.isEmpty()) {
            purchases = IterableUtils.toList(purchaseRepository.findAllByIdClientAndCategoryIsNull(idClients)); //category is empty, with client

        } else if (idCategories != null && !idCategories.isEmpty() && idProducts == null && idClients != null && !idClients.isEmpty()) {
            purchases = IterableUtils.toList(purchaseRepository.findAllByIdClientAndCategory(idCategories, idClients)); // with category, with client

        } else if (idCategories == null && idProducts != null && !idProducts.isEmpty() && idClients != null && !idClients.isEmpty()) {
            purchases = IterableUtils.toList(purchaseRepository.findAllByIdProductAndClient(idProducts, idClients)); // with product, with client

        } else {
            throw new OnlineShopExceptionOld("Неккоректная операция");
        }
        if (purchases == null) {
            throw new OnlineShopExceptionOld("Товаров, удовлевторяющих заданные требования, не сущесвтует");
        }
        //Objects.requireNonNull(purchases).stream().sorted(Comparator.comparing(p -> p.getIdClientAndProduct().getIdClient().getId()));
        Map<Integer, PurchaseResponse> mapPurchase = new HashMap<>();
        PurchaseResponse response;
        Client client;
        InformarionAdoutClientsForAdminResponse clientResponse;
        for (Purchase purchase : purchases) {
            client = purchase.getIdClientAndProduct().getIdClient();
            clientResponse = new InformarionAdoutClientsForAdminResponse(client.getId(), client.getFirstName(),
                    client.getLastName(), client.getPatronymic(), client.getEmail(), client.getPhoneNumber(), client.getPostalAddress());
            response = new PurchaseResponse(clientResponse);
            mapPurchase.put(response.getClient().getId(), response);
        }
        ProductResponse productResponse;
        Product product;
        Integer allCoast;
        for (Purchase purchase : purchases) {
            client = purchase.getIdClientAndProduct().getIdClient();
            product = purchase.getIdClientAndProduct().getIdProduct();
            productResponse = new ProductResponse(product.getId(), product.getName(), product.getPrice(),
                    purchase.getAmount(), product.getCategories());
            mapPurchase.get(client.getId()).getProducts().add(productResponse);
            allCoast = mapPurchase.get(client.getId()).getAllCost();
            allCoast += productResponse.getAllCost();
            mapPurchase.get(client.getId()).setAllCost(allCoast);
        }
        return ResponseEntity.ok().body(new ArrayList<>(mapPurchase.values()));
    }
}
