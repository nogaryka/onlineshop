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

import java.util.List;

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
            product.setName();

            //);=new Product(request.getName(), request.getPrice(), request.getCount(), request.getCategoryList());
            product = productRepository.save(product);
        }
    }

    @Override
    public void deleteProduct(String cookie, Integer id) throws OnlineShopException {

    }

    @Override
    public ProductResponse getProductById(String cookie, Integer id) throws OnlineShopException {
        return null;
    }

    @Override
    public List<ProductResponse> getAllProducts(String cookie, ListProductRequest request) throws OnlineShopException {
        return null;
    }
}
