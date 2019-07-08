package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.service.BasketService;
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
@RequestMapping("/api/baskets")
public class BasketController {
    private final BasketService service;

    @Autowired
    public BasketController(BasketService service) {
        this.service = service;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addProductToBasket(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @DeleteMapping(value = "/api/baskets/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteProductFromBasketById(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> editProductAmountInBasket(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getBasket(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/api/purchases/baskets",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> buyProductsToBasket(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }
}




