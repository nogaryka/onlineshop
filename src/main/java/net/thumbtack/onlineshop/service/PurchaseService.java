package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.BuyProductToBasketRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.dto.responce.BuyProductToBasketResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;

import java.util.List;

public interface PurchaseService {
    BuyProductResponse buyProduct(String cookie, BuyProductRequest request) throws OnlineShopExceptionOld;

    BuyProductToBasketResponse buyProductsToBasket(String cookie, List<BuyProductToBasketRequest> request) throws OnlineShopExceptionOld;
}
