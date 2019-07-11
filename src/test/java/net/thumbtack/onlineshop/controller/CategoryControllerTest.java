package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.repository.CategoryRepository;
import net.thumbtack.onlineshop.service.CategoryService;
import net.thumbtack.onlineshop.service.impl.CategoryServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertNotNull;

public class CategoryControllerTest {
    private CategoryController categoryController;

    private CategoryService categoryService;

    private CategoryRepository сategoryRepository;

    @Before
    public void setUp() {
        categoryService = new CategoryServiceImpl(сategoryRepository);
        categoryController = new CategoryController(categoryService);
    }

    @Test
    public void addCategoryTest() {
        ResponseEntity<?> response = categoryController.addCategory("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void getCategoryByIdTest() {
        ResponseEntity<?> response = categoryController.getCategoryById("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void editCategoryTest() {
        ResponseEntity<?> response =categoryController.editCategory("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void deleteCategoryTest() {
        ResponseEntity<?> response = categoryController.deleteCategory("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void getAllCategoriesTest() {
        ResponseEntity<?> response = categoryController.getAllCategories("test", new MockHttpServletRequest());
        assertNotNull(response);
    }
}


