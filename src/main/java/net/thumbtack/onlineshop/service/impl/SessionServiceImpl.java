package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.dto.responce.SettingsServerResponse;
import net.thumbtack.onlineshop.entity.Administrator;
import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.repository.AdministratorRepository;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {
    private final AdministratorRepository administratorRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public SessionServiceImpl(AdministratorRepository administratorRepository, ClientRepository clientRepository) {
        this.administratorRepository = administratorRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public RegistrationUserResponse login(LoginRequest request) throws OnlineShopException {
        Optional<Administrator> administratorOptional = administratorRepository.findByLogin(request.getLogin(), request.getPassword());
        if (administratorOptional == null) {
            //Client client = clientRepository.findByLogin(request.getLogin(), request.getPassword());
            return null;
        } else {
            Administrator administrator = administratorOptional.get();
            return new RegistrationAdminResponse(administrator.getId(),
                    administrator.getFirstName(), administrator.getLastName(), administrator.getPatronymic(),
                    administrator.getLogin(), administrator.getPassword(), administrator.getPost());
        }
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
