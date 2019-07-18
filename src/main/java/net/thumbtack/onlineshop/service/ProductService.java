package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.EditProductRequest;
import net.thumbtack.onlineshop.dto.request.ListProductRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.responce.AddProductResponse;
import net.thumbtack.onlineshop.dto.responce.ProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

import java.util.List;

public interface ProductService {
    AddProductResponse addProduct(String cookie, AddProductRequest request) throws OnlineShopException;

    AddProductResponse editProduct(String cookie, EditProductRequest request, Integer id) throws OnlineShopException;

    void deleteProduct(String cookie, Integer id) throws OnlineShopException;

    AddProductResponse getProductById(String cookie, Integer id) throws OnlineShopException;

    List<AddProductResponse> getAllProducts(String cookie, Integer[] category, String paramOrder) throws OnlineShopException;
}
