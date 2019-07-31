package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static net.thumbtack.onlineshop.config.ConstConfig.COOKIE;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {
    private final DepositService depositService;

    @Autowired
    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putMoney(@CookieValue(COOKIE) String cookie,
                                      @Valid @RequestBody DepositRequest request) {
        RegistrationClientResponse response = depositService.putMoney(cookie, request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMoney(@CookieValue(COOKIE) String cookie) {
        RegistrationClientResponse response = depositService.getMoney(cookie);
        return ResponseEntity.ok().body(response);
    }
}
