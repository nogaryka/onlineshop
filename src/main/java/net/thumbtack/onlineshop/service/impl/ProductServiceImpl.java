package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.EditProductRequest;
import net.thumbtack.onlineshop.dto.request.ListProductRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.responce.AddProductResponse;
import net.thumbtack.onlineshop.dto.responce.ProductResponse;
import net.thumbtack.onlineshop.entity.Product;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
import net.thumbtack.onlineshop.repository.ProductRepository;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public AddProductResponse addProduct(String cookie, AddProductRequest request) throws OnlineShopExceptionOld {
        Product product = new Product(request.getName(), request.getPrice(), request.getCount(), request.getCategoryList());
        product = productRepository.save(product);
        return new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                product.getCategories());
    }

    @Override
    public AddProductResponse editProduct(String cookie, EditProductRequest request, Integer id) throws OnlineShopExceptionOld {
        if (productRepository.existsById(id)) {
            Product product = productRepository.findById(id).get();
            product.setName(request.getName().equals("") ? product.getName() : request.getName());
            product.setPrice(request.getPrice() == null ? product.getPrice() : request.getPrice());
            product.setCount(request.getCount() == null ? product.getCount() : request.getCount());
            product.setCategories(request.getCategoryList() == null ? product.getCategories() : request.getCategoryList());
            product = productRepository.save(product);
            return new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                    product.getCategories());
        }
        return null;
    }

    @Override
    public void deleteProduct(String cookie, Integer id) throws OnlineShopExceptionOld {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new OnlineShopExceptionOld();
        }
    }

    @Override
    public AddProductResponse getProductById(String cookie, Integer id) throws OnlineShopExceptionOld {
        if (productRepository.existsById(id)) {
            Product product = productRepository.findById(id).get();
            return new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                    product.getCategories());
        }
        return null;
    }

    @Override
    public Set<AddProductResponse> getAllProducts(String cookie, Iterable<Integer> category, String paramOrder) throws OnlineShopExceptionOld {
        Set<AddProductResponse> productSet = new TreeSet<>();
        switch (paramOrder) {
            case "product":
                //Iterable<Product> products = productRepository.findAllByCategory(category);
                /*for (Product product : products) {
                    productSet.add(new AddProductResponse(product.getId(), product.getName(),
                            product.getPrice(), product.getCount(), product.getCategories()));
                }*/
                return productSet;
            case "category":
                return null;
        }
        return null;
    }
}
