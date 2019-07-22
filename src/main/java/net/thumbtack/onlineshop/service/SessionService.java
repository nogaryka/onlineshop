package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.dto.responce.SettingsServerResponse;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;

public interface SessionService {
    RegistrationUserResponse login(LoginRequest request) throws OnlineShopExceptionOld;

    void logout(String cookie) throws OnlineShopExceptionOld;

    SettingsServerResponse getSettingsServer(String cookie) throws OnlineShopExceptionOld;

    void clearDB() throws OnlineShopExceptionOld;
}
