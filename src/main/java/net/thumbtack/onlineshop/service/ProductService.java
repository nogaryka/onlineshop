package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.ListProductRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.request.SessionCookieRequest;
import net.thumbtack.onlineshop.dto.responce.ProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

import java.util.List;

public interface ProductService {
    ProductResponse addProduct(SessionCookieRequest cookie, ProductRequest request) throws OnlineShopException;

    ProductResponse editProduct(SessionCookieRequest cookie, ProductRequest request, Integer id) throws OnlineShopException;

    void deleteProduct(SessionCookieRequest cookie, Integer id) throws OnlineShopException;

    ProductResponse getProductById(SessionCookieRequest cookie, Integer id) throws OnlineShopException;

    List<ProductResponse> getAllProducts(SessionCookieRequest cookie, ListProductRequest request) throws OnlineShopException;
}
