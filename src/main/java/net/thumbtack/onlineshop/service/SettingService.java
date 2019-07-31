package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.responce.SettingResponse;

public interface SettingService {
    SettingResponse getSettings(String cookie);
}
