package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.CategoryRequest;
import net.thumbtack.onlineshop.dto.request.SessionCookieRequest;
import net.thumbtack.onlineshop.dto.responce.CategoryResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

import java.util.List;

public interface CategoryService {
    CategoryResponse addCategory(SessionCookieRequest cookie, CategoryRequest request) throws OnlineShopException;

    CategoryResponse getCategoryById(SessionCookieRequest cookie, Integer id) throws OnlineShopException;

    CategoryResponse editCategory(SessionCookieRequest cookie, CategoryRequest request, Integer id) throws OnlineShopException;

    void deleteCategoryById(SessionCookieRequest cookie, Integer id) throws OnlineShopException;

    List<CategoryResponse> getCategoryList(SessionCookieRequest cookie) throws OnlineShopException;
}


