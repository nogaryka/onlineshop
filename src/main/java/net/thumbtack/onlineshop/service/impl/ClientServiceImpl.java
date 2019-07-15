package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.request.EditAccountClientRequest;
import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationClientRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationUserRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.dto.responce.InformarionAdoutClientsForAdminResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.entity.Administrator;
import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.repository.AdministratorRepository;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final AdministratorRepository administratorRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, AdministratorRepository administratorRepository,
                             SessionRepository sessionRepository) {
        this.clientRepository = clientRepository;
        this.administratorRepository = administratorRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public RegistrationClientResponse addClient(RegistrationClientRequest request) throws OnlineShopException {
        Client client = new Client(request.getFirstName(), request.getLastName(), request.getPatronymic(),
                request.getLogin(), request.getPassword(), request.getEmail(), request.getPhoneNumber(),
                request.getPostalAddress());


        if(administratorRepository.existsByLogin(client.getLogin())) {
            throw new OnlineShopException();
        }
        clientRepository.save(client);
        RegistrationClientResponse response = (RegistrationClientResponse) new SessionServiceImpl(administratorRepository,
                clientRepository, sessionRepository).login(new LoginRequest(client.getLogin(),
                client.getPassword()));
        return response;
    }

    @Override
    public RegistrationClientResponse editProfileClient(String cookie, EditAccountClientRequest request) throws OnlineShopException {
        Client client = clientRepository.findByLogin(sessionRepository.findByToken(cookie).get().getLogin()).get();
        clientRepository.editClient(client.getId(), request.getFirstName(), request.getLastName(),
                request.getPatronymic(), request.getNewPassword(), request.getEmail(), request.getPostalAddress(),
                request.getPhoneNumber());
        return new RegistrationClientResponse(client.getId(), request.getFirstName(),
                request.getLastName(), request.getPatronymic(), client.getLogin(), request.getNewPassword(),
                cookie, request.getEmail(),  request.getPhoneNumber(), request.getPostalAddress());
    }

    @Override
    public List<InformarionAdoutClientsForAdminResponse> getInfoAboutClientsForAdmin(String cookie) throws OnlineShopException {
        if(administratorRepository.existsByLogin(sessionRepository.findByToken(cookie).get().getLogin())) {
            List<InformarionAdoutClientsForAdminResponse> clients = new ArrayList<>();
            Iterable<Client> list = clientRepository.findAll();
            for (Client client : list) {
                clients.add(new InformarionAdoutClientsForAdminResponse(client.getId(), client.getFirstName(),
                        client.getLastName(), client.getPatronymic(), client.getEmail(), client.getPostalAddress(),
                        client.getPhoneNumber()));
            }
            return clients;
        } else {
            throw new OnlineShopException();
        }
    }

    @Override
    public RegistrationUserResponse putMoney(String cookie, DepositRequest request) throws OnlineShopException {
        return null;
    }

    @Override
    public RegistrationUserResponse getMoney(String cookie) throws OnlineShopException {
        return null;
    }

    @Override
    public BuyProductResponse buyProduct(String cookie, BuyProductRequest request) throws OnlineShopException {
        return null;
    }
}
