package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.BuyProductToBasketRequest;
import net.thumbtack.onlineshop.dto.responce.BasketOrPurchaseResponse;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.dto.responce.BuyProductToBasketResponse;
import net.thumbtack.onlineshop.dto.responce.InformarionAdoutClientsForAdminResponse;
import net.thumbtack.onlineshop.dto.responce.ProductResponse;
import net.thumbtack.onlineshop.dto.responce.SummaryBasketListResponse;
import net.thumbtack.onlineshop.dto.responce.SummaryPurchaseListResponse;
import net.thumbtack.onlineshop.entity.Basket;
import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.entity.Product;
import net.thumbtack.onlineshop.entity.Purchase;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
import net.thumbtack.onlineshop.repository.AdministratorRepository;
import net.thumbtack.onlineshop.repository.BasketRepository;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.ProductRepository;
import net.thumbtack.onlineshop.repository.PurchaseRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.PurchaseService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.thumbtack.onlineshop.exceptions.ErrorCod.INCORRECT_OPERATTION;
import static net.thumbtack.onlineshop.exceptions.ErrorCod.LIST_OF_CATEGORIES_AND_PRODUCTS;
import static net.thumbtack.onlineshop.exceptions.ErrorCod.PURCHASE_OR_BASKET;
import static net.thumbtack.onlineshop.exceptions.ErrorCod.THERE_ARE_NO_PURCHASES;
import static net.thumbtack.onlineshop.exceptions.ErrorCod.UNAUTHORIZED_ATTEMPT_TO_ACCESS;
import static net.thumbtack.onlineshop.exceptions.FieldName.MOD;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final ProductRepository productRepository;
    private final SessionRepository sessionRepository;
    private final ClientRepository clientRepository;
    private final BasketRepository basketRepository;
    private final PurchaseRepository purchaseRepository;
    private final SessionServiceImpl sessionService;
    private final AdministratorRepository administratorRepository;

    @Autowired
    public PurchaseServiceImpl(ProductRepository productRepository, SessionRepository sessionRepository,
                               ClientRepository clientRepository, BasketRepository basketRepository,
                               PurchaseRepository purchaseRepository, SessionServiceImpl sessionService,
                               AdministratorRepository administratorRepository) {
        this.productRepository = productRepository;
        this.sessionRepository = sessionRepository;
        this.clientRepository = clientRepository;
        this.basketRepository = basketRepository;
        this.purchaseRepository = purchaseRepository;
        this.sessionService = sessionService;
        this.administratorRepository = administratorRepository;
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
            if (purchaseRepository.existsByClientAndProduct(client.getId(), product.getId()) == 1) {
                Purchase newPurchase = purchaseRepository.findByClientAndProduct(client.getId(), product.getId()).get();
                newPurchase.setAmount(purchase.getAmount() + newPurchase.getAmount());
                newPurchase.setTotalPricePerItem(purchase.getTotalPricePerItem() + newPurchase.getTotalPricePerItem());
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
                sessionRepository, clientRepository, sessionService).getBasket(cookie);
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

    private List<Purchase> requestSummaryListBuilderForPurchase(List<Integer> idCategories, List<Integer> idProducts,
                                                     List<Integer> idClients, Integer offset, Integer limit) {

        if (offset == null) {
            offset = 0;
        }
        if (limit == null || limit == 0) {
            limit = (int) purchaseRepository.count();
        }
        if (idCategories != null && idProducts != null) {
            throw new OnlineShopExceptionOld(LIST_OF_CATEGORIES_AND_PRODUCTS);
        }
        List<Purchase> purchases = null;
        if (idCategories == null && idProducts == null && idClients == null) {
            purchases = IterableUtils.toList(purchaseRepository.findAll(offset, limit));

        } else if (idCategories != null && idCategories.isEmpty() && idProducts == null && idClients == null) {
            purchases = IterableUtils.toList(purchaseRepository.findAllByCategoryIsNull(offset, limit)); //all null category empty

        } else if (idCategories != null && !idCategories.isEmpty() && idProducts == null && idClients == null) {
            purchases = IterableUtils.toList(purchaseRepository.findAllByCategory(idCategories, offset, limit)); //all null with category

        } else if (idCategories == null && idProducts != null && !idProducts.isEmpty() && idClients == null) {
            purchases = IterableUtils.toList(purchaseRepository.findAllByIdProduct(idProducts, offset, limit)); //all null with product

        } else if (idCategories == null && idProducts == null && idClients != null && !idClients.isEmpty()) {
            purchases = IterableUtils.toList(purchaseRepository.findAllByIdClient(idClients, offset, limit)); //all null with client

        } else if (idCategories != null && idCategories.isEmpty() && idProducts == null && idClients != null && !idClients.isEmpty()) {
            purchases = IterableUtils.toList(purchaseRepository.findAllByIdClientAndCategoryIsNull(idClients, offset, limit)); //category is empty, with client

        } else if (idCategories != null && !idCategories.isEmpty() && idProducts == null && idClients != null && !idClients.isEmpty()) {
            purchases = IterableUtils.toList(purchaseRepository
                    .findAllByIdClientAndCategory(idCategories, idClients, offset, limit)); // with category, with client

        } else if (idCategories == null && idProducts != null && !idProducts.isEmpty() && idClients != null && !idClients.isEmpty()) {
            purchases = IterableUtils.toList(purchaseRepository
                    .findAllByIdProductAndClient(idProducts, idClients, offset, limit)); // with product, with client
        } else {
            throw new OnlineShopExceptionOld(INCORRECT_OPERATTION);
        }
        return purchases;
    }

    private List<Basket> requestSummaryListBuilderForBasket(List<Integer> idCategories, List<Integer> idProducts,
                                                                List<Integer> idClients, Integer offset, Integer limit) {

        if (offset == null) {
            offset = 0;
        }
        if (limit == null || limit == 0) {
            limit = (int) basketRepository.count();
        }
        if (idCategories != null && idProducts != null) {
            throw new OnlineShopExceptionOld(LIST_OF_CATEGORIES_AND_PRODUCTS);
        }
        List<Basket> baskets = null;
        if (idCategories == null && idProducts == null && idClients == null) {
            baskets = IterableUtils.toList(basketRepository.findAll(offset, limit));

        } else if (idCategories != null && idCategories.isEmpty() && idProducts == null && idClients == null) {
            baskets = IterableUtils.toList(basketRepository.findAllByCategoryIsNull(offset, limit)); //all null category empty

        } else if (idCategories != null && !idCategories.isEmpty() && idProducts == null && idClients == null) {
            baskets = IterableUtils.toList(basketRepository.findAllByCategory(idCategories, offset, limit)); //all null with category

        } else if (idCategories == null && idProducts != null && !idProducts.isEmpty() && idClients == null) {
            baskets = IterableUtils.toList(basketRepository.findAllByIdProduct(idProducts, offset, limit)); //all null with product

        } else if (idCategories == null && idProducts == null && idClients != null && !idClients.isEmpty()) {
            baskets = IterableUtils.toList(basketRepository.findAllByIdClient(idClients, offset, limit)); //all null with client

        } else if (idCategories != null && idCategories.isEmpty() && idProducts == null && idClients != null && !idClients.isEmpty()) {
            baskets = IterableUtils.toList(basketRepository.findAllByIdClientAndCategoryIsNull(idClients, offset, limit)); //category is empty, with client

        } else if (idCategories != null && !idCategories.isEmpty() && idProducts == null && idClients != null && !idClients.isEmpty()) {
            baskets = IterableUtils.toList(basketRepository
                    .findAllByIdClientAndCategory(idCategories, idClients, offset, limit)); // with category, with client

        } else if (idCategories == null && idProducts != null && !idProducts.isEmpty() && idClients != null && !idClients.isEmpty()) {
            baskets = IterableUtils.toList(basketRepository
                    .findAllByIdProductAndClient(idProducts, idClients, offset, limit)); // with product, with client
        } else {
            throw new OnlineShopExceptionOld(INCORRECT_OPERATTION);
        }
        return baskets;
    }




    @Override
    public ResponseEntity<?> getSummaryList(String cookie, List<Integer> idCategories, List<Integer> idProducts,
                                            List<Integer> idClients, Integer offset, Integer limit, String mod) {
        Session session = sessionService.validCookie(cookie);
        if (!administratorRepository.existsByLogin(session.getLogin())) {
            throw new OnlineShopExceptionOld(UNAUTHORIZED_ATTEMPT_TO_ACCESS);
        }
        List<Purchase> purchases;
        List<Basket> baskets;
        switch (mod) {
            case "Purchase":
                purchases = requestSummaryListBuilderForPurchase(idCategories, idProducts, idClients, offset, limit);
                return getSummaryPurchaseList(purchases);
            case "Basket":
                baskets = requestSummaryListBuilderForBasket(idCategories, idProducts, idClients, offset, limit);
                return getSummaryBasketList(baskets);
            default:
                throw new OnlineShopExceptionOld(MOD, PURCHASE_OR_BASKET);
        }
    }

    private ResponseEntity<?> getSummaryPurchaseList(List<Purchase> purchases) {
        if (purchases == null) {
            throw new OnlineShopExceptionOld(THERE_ARE_NO_PURCHASES);
        }
        Map<Integer, BasketOrPurchaseResponse> mapResponse = new HashMap<>();
        BasketOrPurchaseResponse response;
        Client client;
        InformarionAdoutClientsForAdminResponse clientResponse;
        for (Purchase purchase : purchases) {
            client = purchase.getIdClientAndProduct().getIdClient();
            clientResponse = new InformarionAdoutClientsForAdminResponse(client.getId(), client.getFirstName(),
                    client.getLastName(), client.getPatronymic(), client.getEmail(), client.getPhoneNumber(), client.getPostalAddress());
            response = new BasketOrPurchaseResponse(clientResponse);
            mapResponse.put(response.getClient().getId(), response);
        }
        ProductResponse productResponse;
        Product product;
        Integer totalExpenseForClient;
        Integer totalAmountOfAllPurchases = 0;
        for (Purchase purchase : purchases) {
            client = purchase.getIdClientAndProduct().getIdClient();
            product = purchase.getIdClientAndProduct().getIdProduct();
            productResponse = new ProductResponse(product.getId(), product.getName(), product.getPrice(),
                    purchase.getAmount(), product.getCategories());
            mapResponse.get(client.getId()).getProducts().add(productResponse);
            totalExpenseForClient = mapResponse.get(client.getId()).getTotalExpenseForClient();
            totalExpenseForClient += productResponse.getTotalPricePerItem();
            mapResponse.get(client.getId()).setTotalExpenseForClient(totalExpenseForClient);
            totalAmountOfAllPurchases += productResponse.getTotalPricePerItem();
        }
        return ResponseEntity
                .ok(new SummaryPurchaseListResponse(new ArrayList<>(mapResponse.values()), totalAmountOfAllPurchases));
    }

    private ResponseEntity<?> getSummaryBasketList(List<Basket> baskets) {
        if (baskets == null) {
            throw new OnlineShopExceptionOld(THERE_ARE_NO_PURCHASES);
        }
        Map<Integer, BasketOrPurchaseResponse> mapResponse = new HashMap<>();
        BasketOrPurchaseResponse response;
        Client client;
        InformarionAdoutClientsForAdminResponse clientResponse;
        for (Basket basket : baskets) {
            client = basket.getIdClientAndProduct().getIdClient();
            clientResponse = new InformarionAdoutClientsForAdminResponse(client.getId(), client.getFirstName(),
                    client.getLastName(), client.getPatronymic(), client.getEmail(), client.getPhoneNumber(), client.getPostalAddress());
            response = new BasketOrPurchaseResponse(clientResponse);
            mapResponse.put(response.getClient().getId(), response);
        }
        ProductResponse productResponseInBasket;
        List<ProductResponse>  productResponseList = new ArrayList<>();
        Product product;
        Integer totalExpenseForClient;
        Integer totalAmountOfAllBaskets = 0;
        Integer totalAmountOfAllBasketsNotSuffice = 0;
        for (Basket basket : baskets) {
            client = basket.getIdClientAndProduct().getIdClient();
            product = basket.getIdClientAndProduct().getIdProduct();
            if(basket.getAmount() > product.getCount()) {
                productResponseList.add(new ProductResponse(product.getId(), product.getName(), product.getPrice(),
                        basket.getAmount() - product.getCount(), product.getCategories()));
                totalAmountOfAllBasketsNotSuffice += (basket.getAmount() - product.getCount()) * product.getPrice();
            }
            productResponseInBasket = new ProductResponse(product.getId(), product.getName(), product.getPrice(),
                    basket.getAmount(), product.getCategories());
            mapResponse.get(client.getId()).getProducts().add(productResponseInBasket);
            totalExpenseForClient = mapResponse.get(client.getId()).getTotalExpenseForClient();
            totalExpenseForClient += productResponseInBasket.getTotalPricePerItem();
            mapResponse.get(client.getId()).setTotalExpenseForClient(totalExpenseForClient);
            totalAmountOfAllBaskets += productResponseInBasket.getTotalPricePerItem();
        }
        return ResponseEntity
                .ok(new SummaryBasketListResponse(new ArrayList<>(mapResponse.values()), totalAmountOfAllBaskets,
                        new ArrayList<>(productResponseList), totalAmountOfAllBasketsNotSuffice));
    }
}
