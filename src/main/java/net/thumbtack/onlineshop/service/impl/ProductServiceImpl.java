package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.EditProductRequest;
import net.thumbtack.onlineshop.dto.request.ListProductRequest;
import net.thumbtack.onlineshop.dto.request.ProductRequest;
import net.thumbtack.onlineshop.dto.responce.AddProductResponse;
import net.thumbtack.onlineshop.dto.responce.ProductResponse;
import net.thumbtack.onlineshop.entity.Product;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.repository.ProductRepository;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public AddProductResponse addProduct(String cookie, AddProductRequest request) throws OnlineShopException {
        Product product = new Product(request.getName(), request.getPrice(), request.getCount(), request.getCategoryList());
        product = productRepository.save(product);
        return new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                product.getCategories());
    }

    @Override
    public AddProductResponse editProduct(String cookie, EditProductRequest request, Integer id) throws OnlineShopException {
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
    public void deleteProduct(String cookie, Integer id) throws OnlineShopException {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new OnlineShopException();
        }
    }

    @Override
    public AddProductResponse getProductById(String cookie, Integer id) throws OnlineShopException {
        if (productRepository.existsById(id)) {
            Product product = productRepository.findById(id).get();
            return new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                    product.getCategories());
        }
        return null;
    }

    @Override
    public List<AddProductResponse> getAllProducts(String cookie, Integer[] category, String paramOrder) throws OnlineShopException {
        Set<AddProductResponse> responseList = new HashSet<>();
        for (Integer idCategory : category) {

            Iterable<Product> products = productRepository.findByIdCategory();
            for (Product product : products) {
                responseList.add(new AddProductResponse(product.getId(), product.getName(), product.getPrice(),
                        product.getCount(), product.getCategories()));
            }
        }
        return responseList;
    }
}
