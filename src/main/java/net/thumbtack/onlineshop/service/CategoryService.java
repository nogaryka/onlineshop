package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.request.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.responce.AddCategoryResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;

import java.util.List;

public interface CategoryService {
    AddCategoryResponse addCategory(String cookie, AddCategoryRequest request) throws OnlineShopExceptionOld;

    AddCategoryResponse getCategoryById(String cookie, Integer id) throws OnlineShopExceptionOld;

    AddCategoryResponse editCategory(String cookie, EditCategoryRequest request, Integer id) throws OnlineShopExceptionOld;

    void deleteCategoryById(String cookie, Integer id) throws OnlineShopExceptionOld;

    List<AddCategoryResponse> getCategoryList(String cookie) throws OnlineShopExceptionOld;
}


