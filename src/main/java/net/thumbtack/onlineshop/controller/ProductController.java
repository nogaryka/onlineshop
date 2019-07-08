package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addProduct(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @PutMapping(value = "/api/products/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> editProduct(@RequestBody String name, HttpServletRequest response) {
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
