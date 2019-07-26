package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/baskets")
public class BasketController {
    private final BasketService service;

    @Autowired
    public BasketController(BasketService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProductToBasket(@CookieValue(OnlineShopServer.COOKIE) String cookie,
                                                @Valid @RequestBody BuyProductRequest request) {
        List<BuyProductResponse> response = service.addProductToBasket(cookie, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/api/baskets/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProductFromBasketById(@CookieValue(OnlineShopServer.COOKIE) String cookie,
                                                         @PathVariable Integer id) {
        service.deleteProductFromBasketById(cookie, id);
        return ResponseEntity.ok("");
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editProductAmountInBasket(@CookieValue(OnlineShopServer.COOKIE) String cookie,
                                                       @Valid @RequestBody BuyProductRequest request) {
        BuyProductResponse response = service.editProductAmountInBasket(cookie, request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBasket(@CookieValue(OnlineShopServer.COOKIE) String cookie) {
        List<BuyProductResponse> response = service.getBasket(cookie);
        return ResponseEntity.ok().body(response);
    }
}




