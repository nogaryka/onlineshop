package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.request.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.responce.AddCategoryResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

import java.util.List;

public interface CategoryService {
    AddCategoryResponse addCategory(String cookie, AddCategoryRequest request) throws OnlineShopException;

    AddCategoryResponse getCategoryById(String cookie, Integer id) throws OnlineShopException;

    AddCategoryResponse editCategory(String cookie, EditCategoryRequest request, Integer id) throws OnlineShopException;

    void deleteCategoryById(String cookie, Integer id) throws OnlineShopException;

    List<AddCategoryResponse> getCategoryList(String cookie) throws OnlineShopException;
}


