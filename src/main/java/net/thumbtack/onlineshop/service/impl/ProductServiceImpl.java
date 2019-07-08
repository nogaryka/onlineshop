package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.ListProductRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.request.SessionCookieRequest;
import net.thumbtack.onlineshop.dto.responce.ProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.repository.ProductRepository;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse addProduct(SessionCookieRequest cookie, ProductRequest request) throws OnlineShopException {
        return null;
    }

    @Override
    public ProductResponse editProduct(SessionCookieRequest cookie, ProductRequest request, Integer id) throws OnlineShopException {
        return null;
    }

    @Override
    public void deleteProduct(SessionCookieRequest cookie, Integer id) throws OnlineShopException {

    }

    @Override
    public ProductResponse getProductById(SessionCookieRequest cookie, Integer id) throws OnlineShopException {
        return null;
    }

    @Override
    public List<ProductResponse> getAllProducts(SessionCookieRequest cookie, ListProductRequest request) throws OnlineShopException {
        return null;
    }
}
