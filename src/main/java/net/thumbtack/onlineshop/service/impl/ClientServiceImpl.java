package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.request.EditAccountClientRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationUserRequest;
import net.thumbtack.onlineshop.dto.request.SessionCookieRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.dto.responce.InformarionAdoutUserResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public RegistrationUserResponse addClient(RegistrationUserRequest request) throws OnlineShopException {
        return null;
    }

    @Override
    public RegistrationUserResponse editProfileClient(SessionCookieRequest cookie, EditAccountClientRequest request) throws OnlineShopException {
        return null;
    }

    @Override
    public InformarionAdoutUserResponse getInfoAboutClientForAdmin(SessionCookieRequest cookie) throws OnlineShopException {
        return null;
    }

    @Override
    public RegistrationUserResponse putMoney(SessionCookieRequest cookie, DepositRequest request) throws OnlineShopException {
        return null;
    }

    @Override
    public RegistrationUserResponse getMoney(SessionCookieRequest cookie) throws OnlineShopException {
        return null;
    }

    @Override
    public BuyProductResponse buyProduct(SessionCookieRequest cookie, BuyProductRequest request) throws OnlineShopException {
        return null;
    }
}
