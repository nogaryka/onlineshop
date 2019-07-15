package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.UUID;

import static net.thumbtack.onlineshop.OnlineShopServer.COOKIE;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    private final SessionService service;

    @Autowired
    public SessionController(SessionService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        RegistrationUserResponse registrationUserResponse = service.login(request);
        Cookie cookie = new Cookie(COOKIE,  registrationUserResponse.getToken());
        response.addCookie(cookie);
        return ResponseEntity.ok().body(registrationUserResponse);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(@CookieValue(COOKIE) String coolie) {
        System.out.println(coolie);
        service.logout(coolie);
        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/api/settings",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getSettingsServer(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/api/debug/clear",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> clearDB(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }
}