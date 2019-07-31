package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.request.EditAccountClientRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationClientRequest;
import net.thumbtack.onlineshop.dto.responce.InformarionAdoutClientsForAdminResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.service.ClientService;
import net.thumbtack.onlineshop.service.SessionService;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static net.thumbtack.onlineshop.config.ConstConfig.COOKIE;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService serviceClient;
    private final SessionService service;

    @Autowired
    public ClientController(ClientService serviceClient, SessionService service) {
        this.serviceClient = serviceClient;
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addClient(@Valid @RequestBody RegistrationClientRequest request,
                                       HttpServletResponse response) {
        RegistrationClientResponse clientResponse = serviceClient.addClient(request);
        Cookie cookie = new Cookie(COOKIE, clientResponse.getToken());
        response.addCookie(cookie);
        return ResponseEntity.ok().body(clientResponse);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editProfileClient(@CookieValue(COOKIE) String cookie,
                                               @Valid @RequestBody EditAccountClientRequest request) {
        RegistrationClientResponse registerResponse = serviceClient.editProfileClient(cookie, request);
        return ResponseEntity.ok().body(registerResponse);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getInfoAboutClientsForAdmin(@CookieValue(COOKIE) String cookie) {
        List<InformarionAdoutClientsForAdminResponse> clients = serviceClient.getInfoAboutClientsForAdmin(cookie);
        return ResponseEntity.ok().body(clients);
    }
}
