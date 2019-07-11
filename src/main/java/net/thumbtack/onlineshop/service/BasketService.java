package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

import java.util.List;

public interface BasketService {
    BuyProductResponse addProductToBasket(String cookie, BuyProductRequest request) throws OnlineShopException;

    void deleteProductFromBasketById(String cookie, Integer id) throws OnlineShopException;

    BuyProductResponse editProductAmountInBasket(String cookie, BuyProductRequest request) throws OnlineShopException;

    List<BuyProductResponse> getBasket(String cookie) throws OnlineShopException;

    List<BuyProductResponse> buyProductsToBasket(String cookie, List<BuyProductRequest> request) throws OnlineShopException;
}
