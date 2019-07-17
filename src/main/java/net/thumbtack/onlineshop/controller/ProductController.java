package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.EditAccountAdminRequest;
import net.thumbtack.onlineshop.dto.request.EditProductRequest;
import net.thumbtack.onlineshop.dto.responce.AddProductResponse;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static net.thumbtack.onlineshop.OnlineShopServer.COOKIE;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProduct(@CookieValue(COOKIE) String cookie,
                                        @Valid @RequestBody AddProductRequest request) {
        AddProductResponse response = productService.addProduct(cookie, request);
        return ResponseEntity.ok("");
    }

    @PutMapping(value = "/api/products/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editProduct(@CookieValue(COOKIE) String cookie,
                                         @Valid @RequestBody EditProductRequest request, Integer id) {
        AddProductResponse response = productService.editProduct(cookie, request, id);
        return ResponseEntity.ok("");
    }

    @DeleteMapping(value = "/api/products/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteProduct(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/api/products/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getProductById(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllProducts(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }
}
