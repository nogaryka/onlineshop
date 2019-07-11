package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.request.EditAccountClientRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationUserRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.dto.responce.InformarionAdoutUserResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

public interface ClientService {
    RegistrationUserResponse addClient(RegistrationUserRequest request) throws OnlineShopException;

    RegistrationUserResponse editProfileClient(String cookie, EditAccountClientRequest request) throws OnlineShopException;

    InformarionAdoutUserResponse getInfoAboutClientForAdmin(String cookie) throws OnlineShopException;

    RegistrationUserResponse putMoney(String cookie, DepositRequest request) throws OnlineShopException;

    RegistrationUserResponse getMoney(String cookie) throws OnlineShopException;

    BuyProductResponse buyProduct(String cookie, BuyProductRequest request) throws OnlineShopException;
}