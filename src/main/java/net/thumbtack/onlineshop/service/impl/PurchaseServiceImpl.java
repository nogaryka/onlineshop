package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.BuyProductToBasketRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.dto.responce.BuyProductToBasketResponse;
import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.entity.Product;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
import net.thumbtack.onlineshop.repository.BasketRepository;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.ProductRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.PurchaseService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PurchaseServiceImpl(ProductRepository productRepository, SessionRepository sessionRepository, ClientRepository clientRepository, BasketRepository basketRepository) {
        this.productRepository = productRepository;
        this.sessionRepository = sessionRepository;
        this.clientRepository = clientRepository;
        this.basketRepository = basketRepository;
    }

    @Override
    public BuyProductResponse buyProduct(String cookie, BuyProductRequest request) throws OnlineShopExceptionOld {
        Session session = sessionRepository.findByToken(cookie).get();
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        if (productRepository.existsById(request.getId())) {
            Product product = productRepository.findById(request.getId()).get();
            if (product.getCount() < request.getCount()) {
                throw new OnlineShopExceptionOld();
            } else if (client.getCash() < request.getCount() * request.getPrice()) {
                throw new OnlineShopExceptionOld();
            } else if (!request.getName().equals(product.getName()) || !request.getPrice().equals(product.getPrice())) {
                throw new OnlineShopExceptionOld();
            }
            product.setCount(product.getCount() - request.getCount());
            productRepository.save(product);
            client.setCash(client.getCash() - request.getCount() * request.getPrice());
            clientRepository.save(client);
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

        for (Map.Entry<Integer, BuyProductToBasketRequest> productRequestEntry : mapRequest.entrySet()) {
            productRequest = productRequestEntry.getValue();
            if (!mapProductInBasket.containsKey(productRequest.getId())) { //если нет в корзине такого, то ну его нахуй
                mapRequest.remove(productRequest.getId());
            }
            productInBasket = mapProductInBasket.get(productRequest.getId());
            if (productRequest.getName().equals(productInBasket.getName()) || // если названаие или цена за штуку не совпадают, то ну его нахуй
                    productRequest.getPrice().equals(productInBasket.getPrice())) {
                mapRequest.remove(productRequest.getId());
            }
            if (productRequest.getCount() == null) {
                productRequest.setCount(mapProductInBasket.get(productRequest.getId()).getCount()); //если количество покупаемых товаров в запросе не указано, то покупает все из корзины
            }

        }


        /*Iterable<Integer> idProducts = request.stream().map(BuyProductToBasketRequest::getId).collect(Collectors.toList());
        listProductInBasket.sort(Comparator.comparing(BuyProductResponse::getId));
        Iterable<Product> products = productRepository.findAllById(idProducts);
        List<Product> productsList = IterableUtils.toList(products);
        BuyProductToBasketRequest buyProductRequest;
        Product product;
        for (int i = 0; i < productsList.size() - 1; i++) {
            buyProductRequest = request.get(i);
            product = productsList.get(i);
            if (buyProductRequest.getCount() == null) {
   //             buyProductRequest.setCount();
            }
        }*/
        return null;
    }
}
