package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.SessionCookieRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

import java.util.List;

public interface BasketService {
    BuyProductResponse addProductToBasket(SessionCookieRequest cookie, BuyProductRequest request) throws OnlineShopException;

    void deleteProductFromBasketById(SessionCookieRequest cookie, Integer id) throws OnlineShopException;

    BuyProductResponse editProductAmountInBasket(SessionCookieRequest cookie, BuyProductRequest request) throws OnlineShopException;

    List<BuyProductResponse> getBasket(SessionCookieRequest cookie) throws OnlineShopException;

    List<BuyProductResponse> buyProductsToBasket(SessionCookieRequest cookie, List<BuyProductRequest> request) throws OnlineShopException;
}
