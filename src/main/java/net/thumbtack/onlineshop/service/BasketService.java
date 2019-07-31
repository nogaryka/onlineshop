package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;

import java.util.List;

public interface BasketService {
    List<BuyProductResponse> addProductToBasket(String cookie, BuyProductRequest request) throws OnlineShopExceptionOld;

    void deleteProductFromBasketById(String cookie, Integer id) throws OnlineShopExceptionOld;

    List<BuyProductResponse> editProductAmountInBasket(String cookie, BuyProductRequest request) throws OnlineShopExceptionOld;

    List<BuyProductResponse> getBasket(String cookie) throws OnlineShopExceptionOld;
}
