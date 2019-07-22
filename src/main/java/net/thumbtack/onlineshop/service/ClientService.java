package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.request.EditAccountClientRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationClientRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.dto.responce.InformarionAdoutClientsForAdminResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;

import java.util.List;

public interface ClientService {
    RegistrationClientResponse addClient(RegistrationClientRequest request) throws OnlineShopExceptionOld;

    RegistrationClientResponse editProfileClient(String cookie, EditAccountClientRequest request) throws OnlineShopExceptionOld;

    List<InformarionAdoutClientsForAdminResponse> getInfoAboutClientsForAdmin(String cookie) throws OnlineShopExceptionOld;
}