package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;

public interface DepositService {
    RegistrationClientResponse putMoney(String cookie, DepositRequest request) throws OnlineShopExceptionOld;

    RegistrationClientResponse getMoney(String cookie) throws OnlineShopExceptionOld;
}
