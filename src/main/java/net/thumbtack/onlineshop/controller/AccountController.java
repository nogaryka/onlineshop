package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.service.AccaountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.thumbtack.onlineshop.OnlineShopServer.COOKIE;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccaountService accaountService;

    @Autowired
    public AccountController(AccaountService accaountService) {
        this.accaountService = accaountService;
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getInfoAboutMe(@CookieValue(COOKIE) String cookie) {
        RegistrationUserResponse response = accaountService.getInfoAboutMe(cookie);
        return ResponseEntity.ok().body(response);
    }
}
