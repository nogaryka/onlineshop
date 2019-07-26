package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.BuyProductToBasketRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.dto.responce.BuyProductToBasketResponse;
import net.thumbtack.onlineshop.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    private  final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buyProduct(@CookieValue(OnlineShopServer.COOKIE) String cookie,
                                        @Valid @RequestBody BuyProductRequest request) {
        BuyProductResponse response = purchaseService.buyProduct(cookie, request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/baskets",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> buyProductsToBasket(@CookieValue(OnlineShopServer.COOKIE) String cookie,
                                                 @Valid @RequestBody List<BuyProductToBasketRequest> request) {
        BuyProductToBasketResponse response = purchaseService.buyProductsToBasket(cookie, request);
        return ResponseEntity.ok().body(response);
    }
}
