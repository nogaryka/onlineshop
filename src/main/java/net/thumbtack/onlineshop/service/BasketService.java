package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;

import java.util.List;

public interface BasketService {
    BuyProductResponse addProductToBasket(String cookie, BuyProductRequest request) throws OnlineShopExceptionOld;

    void deleteProductFromBasketById(String cookie, Integer id) throws OnlineShopExceptionOld;

    BuyProductResponse editProductAmountInBasket(String cookie, BuyProductRequest request) throws OnlineShopExceptionOld;

    List<BuyProductResponse> getBasket(String cookie) throws OnlineShopExceptionOld;

    List<BuyProductResponse> buyProductsToBasket(String cookie, List<BuyProductRequest> request) throws OnlineShopExceptionOld;
}
