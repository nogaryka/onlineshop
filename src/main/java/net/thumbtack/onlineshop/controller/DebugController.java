package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.service.DebugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug/clear")
public class DebugController {
    private final DebugService debugService;

    @Autowired
    public DebugController(DebugService debugService) {
        this.debugService = debugService;
    }

    @DeleteMapping
    public ResponseEntity<?> getSettings() {
        debugService.clearDB();
        return ResponseEntity.ok("");
    }
}
