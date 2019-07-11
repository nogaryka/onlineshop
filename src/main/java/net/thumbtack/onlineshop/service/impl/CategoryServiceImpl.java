package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.responce.CategoryResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.repository.CategoryRepository;
import net.thumbtack.onlineshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public CategoryResponse addCategory(String cookie, CategoryRequest request) throws OnlineShopException {
        return null;
    }

    @Override
    public CategoryResponse getCategoryById(String cookie, Integer id) throws OnlineShopException {
        return null;
    }

    @Override
    public CategoryResponse editCategory(String cookie, CategoryRequest request, Integer id) throws OnlineShopException {
        return null;
    }

    @Override
    public void deleteCategoryById(String cookie, Integer id) throws OnlineShopException {

    }

    @Override
    public List<CategoryResponse> getCategoryList(String cookie) throws OnlineShopException {
        return null;
    }
}
