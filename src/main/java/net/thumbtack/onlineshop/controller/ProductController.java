package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.EditProductRequest;
import net.thumbtack.onlineshop.dto.responce.AddProductResponse;
import net.thumbtack.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Set;

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
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "/api/products/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editProduct(@CookieValue(COOKIE) String cookie,
                                         @Valid @RequestBody EditProductRequest request, Integer id) {
        AddProductResponse response = productService.editProduct(cookie, request, id);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/api/products/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProduct(@CookieValue(COOKIE) String cookie, Integer id) {
        productService.deleteProduct(cookie, id);
        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/api/products/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProductById(@CookieValue(COOKIE) String cookie, @PathVariable("id") Integer id) {
        AddProductResponse response = productService.getProductById(cookie, id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/api/products",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllProducts(@CookieValue(COOKIE) String cookie,
                                            @RequestParam(value = "category", required = false) Iterable<Integer> category,
                                            @RequestParam(value = "order", required = false, defaultValue = "product") String order) {
        Set<AddProductResponse> responseList = productService.getAllProducts(cookie, category, order);
        return ResponseEntity.ok("");
    }
}
