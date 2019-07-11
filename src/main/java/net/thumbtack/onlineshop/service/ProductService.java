package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.ListProductRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.responce.ProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

import java.util.List;

public interface ProductService {
    ProductResponse addProduct(String cookie, ProductRequest request) throws OnlineShopException;

    ProductResponse editProduct(String cookie, ProductRequest request, Integer id) throws OnlineShopException;

    void deleteProduct(String cookie, Integer id) throws OnlineShopException;

    ProductResponse getProductById(String cookie, Integer id) throws OnlineShopException;

    List<ProductResponse> getAllProducts(String cookie, ListProductRequest request) throws OnlineShopException;
}
