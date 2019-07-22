package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;

public interface PurchaseService {
    BuyProductResponse buyProduct(String cookie, BuyProductRequest request) throws OnlineShopExceptionOld;
}
