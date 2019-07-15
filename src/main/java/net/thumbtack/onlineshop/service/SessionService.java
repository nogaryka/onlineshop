package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.dto.responce.SettingsServerResponse;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

public interface SessionService {
    RegistrationUserResponse login(LoginRequest request) throws OnlineShopException;

    void logout(String cookie) throws OnlineShopException;

    SettingsServerResponse getSettingsServer(String cookie) throws OnlineShopException;

    void clearDB() throws OnlineShopException;
}
