package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.responce.SettingResponse;
import net.thumbtack.onlineshop.service.SettingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static net.thumbtack.onlineshop.config.ConstConfig.PATH_MAX_NAME_LENGTH;
import static net.thumbtack.onlineshop.config.ConstConfig.PATH_MIN_PASSWORD_LENGTH;

@Service
public class SettingServiceImpl implements SettingService {
    @Value(PATH_MAX_NAME_LENGTH)
    private int max_name_length;

    @Value(PATH_MIN_PASSWORD_LENGTH)
    private int min_password_length;

    @Override
    public SettingResponse getSettings(String cookie) {

        return new SettingResponse(max_name_length, min_password_length);
    }
}