package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;

public interface SessionService {
    RegistrationUserResponse login(LoginRequest request) throws OnlineShopExceptionOld;

    void logout(String cookie) throws OnlineShopExceptionOld;
}
