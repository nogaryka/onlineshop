package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.responce.SettingResponse;
import net.thumbtack.onlineshop.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.thumbtack.onlineshop.config.ConstConfig.COOKIE;

@RestController
@RequestMapping("/api/settings")
public class SettingController {
    private final SettingService settingService;

    @Autowired
    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSettings(@CookieValue(value = COOKIE, required = false) String cookie) {
        SettingResponse response = settingService.getSettings(cookie);
        return ResponseEntity.ok().body(response);
    }
}
