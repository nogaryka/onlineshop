package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.EditAccountAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static net.thumbtack.onlineshop.OnlineShopServer.COOKIE;


@RestController
@RequestMapping("/api/admins")
public class AdministratorController {
    private final AdministratorService adminService;

    @Autowired
    public AdministratorController(AdministratorService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addAdmin(@Valid @RequestBody RegistrationAdminRequest request,
                                      HttpServletResponse response) {
        RegistrationAdminResponse registerResponse = adminService.addAdmin(request);
        response.addCookie(new Cookie(COOKIE,  registerResponse.getId().toString()));
        return ResponseEntity.ok().body(registerResponse);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> editProfileAdmin(@CookieValue(COOKIE) String cookie,
                                              @RequestBody EditAccountAdminRequest request) {
        RegistrationAdminResponse registerResponse = adminService.editProfileAdmin(cookie, request);
        return ResponseEntity.ok().body(registerResponse);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> statement(@RequestBody String name,HttpServletRequest response) {
        return ResponseEntity.ok("");
    }
}
