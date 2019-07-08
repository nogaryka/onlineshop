package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService service;

    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addClient(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> editProfileClient(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getInfoAboutClientForAdmin(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @PutMapping(value = "/api/deposits",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> putMoney(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/api/deposits",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getMoney(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/api/purchases",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> buyProduct(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }
}
