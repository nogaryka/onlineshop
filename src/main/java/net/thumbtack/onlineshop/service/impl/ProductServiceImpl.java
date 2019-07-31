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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
        List<Category> categoryList = new ArrayList<>();
        if (request.getIdCategories() != null) {
            categoryList = IterableUtils.toList(categoryRepository.findAllById(request.getIdCategories()));
        }
        Product product = new Product(request.getName(), request.getPrice(), request.getCount(), categoryList);
        product = productRepository.save(product);
        return new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                request.getIdCategories());
    }

    @Override
    public AddProductResponse editProduct(String cookie, EditProductRequest request, Integer id) throws OnlineShopExceptionOld {
        if (productRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new OnlineShopExceptionOld("Продукт с таким названием уже существует");
        }
        if (productRepository.existsById(id)) {
            Product product = productRepository.findById(id).get();
            product.setName(request.getName() == null || request.getName().equals("") ? product.getName() : request.getName());
            product.setPrice(request.getPrice() == null ? product.getPrice() : request.getPrice());
            product.setCount(request.getCount() == null ? product.getCount() : request.getCount());
            List<Category> categoryList;
            List<Integer> idCategory = new ArrayList<>();
            if (request.getIdCategory() != null) {
                categoryList = IterableUtils.toList(categoryRepository.findAllById(request.getIdCategory()));
                product.setCategories(request.getIdCategory() == null ? product.getCategories() : categoryList);
            } else {
                for (Category category : product.getCategories()) {
                    idCategory.add(category.getId());
                }
            }
            product = productRepository.save(product);
            return new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                    idCategory);
        }
        return null;
    }

    @Override
    public void deleteProduct(String cookie, Integer id) throws OnlineShopExceptionOld {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new OnlineShopExceptionOld("Такого продукта не существует");
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
    public List<AddProductResponse> getAllProducts(String cookie, Set<Integer> category, String paramOrder) throws OnlineShopExceptionOld {
        Iterable<Category> categories = null;
        if (category == null && paramOrder.equals("product")) {
            paramOrder = "all";
        } else if (category == null && paramOrder.equals("category")) {
            categories = categoryRepository.findAllByOrderByNameAsc();
        } else if (category.isEmpty()) {
            paramOrder = "categoryLess";
        } else {
            categories = categoryRepository.findAllByIdInOrderByNameAsc(category);
        }
        List<AddProductResponse> responsesProducts = new ArrayList<>();
        Iterable<Product> products;
        switch (paramOrder) {
            case "product":
                products = IterableUtils.toList(productRepository.findDistinctProductsByCategoriesInOrderByNameAsc(categories));
                for (Product product : products) {
                    responsesProducts.add(new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                            product.getCategories().stream()
                                    .map(Category::getId)
                                    .collect(Collectors.toList())));
                }
                break;
            case "category":
                products = IterableUtils.toList(productRepository.findAllByCategoriesIsNullOrderByNameAsc());
                for (Product product : products) {
                    responsesProducts.add(new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount()));
                }
                products = IterableUtils.toList(productRepository.findDistinctProductsByCategoriesInOrderByNameAsc(categories));
                for (Category c : categories) {
                    for (Product product : products) {
                        if (product.getCategories().contains(c)) {
                            responsesProducts.add(new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                                    product.getCategories().stream()
                                            .filter(category1 -> category1.getName().equals(c.getName()))
                                            .map(Category::getId)
                                            .collect(Collectors.toList())));
                        }
                    }
                }
                break;
            case "all":
                products = IterableUtils.toList(productRepository.findAllByOrderByNameAsc());
                for (Product product : products) {
                    responsesProducts.add(new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                            product.getCategories().stream()
                                    .map(Category::getId)
                                    .collect(Collectors.toList())));
                }
                break;
            case "categoryLess":
                products = IterableUtils.toList(productRepository.findAllByCategoriesIsNullOrderByNameAsc());
                for (Product product : products) {
                    responsesProducts.add(new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount()));
                }
                break;

        }
        return responsesProducts;
    }
}
