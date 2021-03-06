package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.EditProductRequest;
import net.thumbtack.onlineshop.dto.responce.AddProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;

import java.util.List;
import java.util.Set;

public interface ProductService {
    AddProductResponse addProduct(String cookie, AddProductRequest request) throws OnlineShopExceptionOld;

    AddProductResponse editProduct(String cookie, EditProductRequest request, Integer id) throws OnlineShopExceptionOld;

    void deleteProduct(String cookie, Integer id) throws OnlineShopExceptionOld;

    AddProductResponse getProductById(String cookie, Integer id) throws OnlineShopExceptionOld;

    List<AddProductResponse> getAllProducts(String cookie, Set<Integer> category, String paramOrder) throws OnlineShopExceptionOld;
}
