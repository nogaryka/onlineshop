package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.EditProductRequest;
import net.thumbtack.onlineshop.dto.responce.AddProductResponse;
import net.thumbtack.onlineshop.entity.Category;
import net.thumbtack.onlineshop.entity.Product;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
import net.thumbtack.onlineshop.repository.CategoryRepository;
import net.thumbtack.onlineshop.repository.ProductRepository;
import net.thumbtack.onlineshop.service.ProductService;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public AddProductResponse addProduct(String cookie, AddProductRequest request) throws OnlineShopExceptionOld {
        List<Category> categoryList;
        categoryList = IterableUtils.toList(categoryRepository.findAllById(request.getIdCategories()));
        Product product = new Product(request.getName(), request.getPrice(), request.getCount(), categoryList);
        product = productRepository.save(product);
        return new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                request.getIdCategories());
    }

    @Override
    public AddProductResponse editProduct(String cookie, EditProductRequest request, Integer id) throws OnlineShopExceptionOld {
        List<Category> categoryList;
        categoryList = IterableUtils.toList(categoryRepository.findAllById(request.getIdCategory()));
        if (productRepository.existsById(id)) {
            Product product = productRepository.findById(id).get();
            product.setName(request.getName().equals("") ? product.getName() : request.getName());
            product.setPrice(request.getPrice() == null ? product.getPrice() : request.getPrice());
            product.setCount(request.getCount() == null ? product.getCount() : request.getCount());
            product.setCategories(request.getIdCategory() == null ? product.getCategories() : categoryList);
            product = productRepository.save(product);
            return new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                    request.getIdCategory());
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
        List<Integer> idCategories = new ArrayList<>();
        if (productRepository.existsById(id)) {
            Product product = productRepository.findById(id).get();
            for (Category category : product.getCategories()) {
                idCategories.add(category.getId());
            }
            return new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                    idCategories);
        }
        return null;
    }

    @Override
    public Set<AddProductResponse> getAllProducts(Set<Integer> category, String paramOrder) throws OnlineShopExceptionOld {
        Iterable<Category> categories = categoryRepository.findAllById(category);
        Iterable<Product> products = productRepository.findDistinctProductsByCategoriesIn(categories);
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
