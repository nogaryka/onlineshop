package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.responce.CategoryResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

import java.util.List;

public interface CategoryService {
    CategoryResponse addCategory(String cookie, CategoryRequest request) throws OnlineShopException;

    CategoryResponse getCategoryById(String cookie, Integer id) throws OnlineShopException;

    CategoryResponse editCategory(String cookie, CategoryRequest request, Integer id) throws OnlineShopException;

    void deleteCategoryById(String cookie, Integer id) throws OnlineShopException;

    List<CategoryResponse> getCategoryList(String cookie) throws OnlineShopException;
}


