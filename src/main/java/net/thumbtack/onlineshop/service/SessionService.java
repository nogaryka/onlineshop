package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.request.SessionCookieRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.dto.responce.SettingsServerResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

public interface SessionService {
    RegistrationUserResponse login(LoginRequest request) throws OnlineShopException;

    void logout(SessionCookieRequest cookie) throws OnlineShopException;

    RegistrationUserResponse getInfoAboutMe(SessionCookieRequest cookie) throws OnlineShopException;

    SettingsServerResponse getSettingsServer(SessionCookieRequest cookie) throws OnlineShopException;

    void clearDB() throws OnlineShopException;
}
