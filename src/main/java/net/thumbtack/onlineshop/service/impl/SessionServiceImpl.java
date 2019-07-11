package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.dto.responce.SettingsServerResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.service.SessionService;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {
    @Override
    public RegistrationUserResponse login(LoginRequest request) throws OnlineShopException {
        return null;
    }

    @Override
    public void logout(String cookie) throws OnlineShopException {

    }

    @Override
    public RegistrationUserResponse getInfoAboutMe(String cookie) throws OnlineShopException {
        return null;
    }

    @Override
    public SettingsServerResponse getSettingsServer(String cookie) throws OnlineShopException {
        return null;
    }

    @Override
    public void clearDB() throws OnlineShopException {

    }
}
