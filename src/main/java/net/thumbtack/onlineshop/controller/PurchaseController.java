package net.thumbtack.onlineshop.controller;

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

import static net.thumbtack.onlineshop.config.ConstConfig.COOKIE;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buyProduct(@CookieValue(COOKIE) String cookie,
                                        @Valid @RequestBody BuyProductRequest request) {
        BuyProductResponse response = purchaseService.buyProduct(cookie, request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/baskets",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> buyProductsToBasket(@CookieValue(COOKIE) String cookie,
                                                 @Valid @RequestBody List<BuyProductToBasketRequest> request) {
        BuyProductToBasketResponse response = purchaseService.buyProductsToBasket(cookie, request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> getSummaryList(@CookieValue(COOKIE) String cookie,
                                             @RequestParam(name = "category", required = false) List<Integer> idCategories,
                                             @RequestParam(name = "product", required = false) List<Integer> idProducts,
                                             @RequestParam(name = "client", required = false) List<Integer> idClients,
                                             @RequestParam(name = "offset", required = false) Integer offset,
                                             @RequestParam(name = "limit", required = false) Integer limit,
                                             @RequestParam(name = "mod", defaultValue = "Purchase") String mod) {
        return purchaseService.getSummaryList(cookie, idCategories, idProducts, idClients, offset, limit, mod);
    }
}
