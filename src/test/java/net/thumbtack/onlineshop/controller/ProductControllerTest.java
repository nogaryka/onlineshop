package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.repository.ProductRepository;
import net.thumbtack.onlineshop.service.ProductService;
import net.thumbtack.onlineshop.service.impl.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertNotNull;

public class ProductControllerTest {
    private ProductController productController;

    private ProductService productService;

    private ProductRepository productRepository;

    @Before
    public void setUp() {
        productService = new ProductServiceImpl(productRepository);
        productController = new ProductController(productService);
    }

    @Test
    public void addProductTest() {
        ResponseEntity<?> response = productController.addProduct("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void editProductTest() {
        ResponseEntity<?> response = productController.editProduct("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void deleteProductTest() {
        ResponseEntity<?> response = productController.deleteProduct("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void getProductByIdTest() {
        ResponseEntity<?> response = productController.getProductById("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void getAllProductsTest() {
        ResponseEntity<?> response = productController.getAllProducts("test", new MockHttpServletRequest());
        assertNotNull(response);
    }
}
