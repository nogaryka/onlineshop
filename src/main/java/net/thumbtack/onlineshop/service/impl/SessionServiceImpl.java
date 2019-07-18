package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.dto.responce.SettingsServerResponse;
import net.thumbtack.onlineshop.entity.Administrator;
import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.repository.AdministratorRepository;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class SessionServiceImpl implements SessionService {
    private final AdministratorRepository administratorRepository;
    private final ClientRepository clientRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionServiceImpl(AdministratorRepository administratorRepository, ClientRepository clientRepository,
                              SessionRepository sessionRepository) {
        this.administratorRepository = administratorRepository;
        this.clientRepository = clientRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public RegistrationUserResponse login(LoginRequest request) throws OnlineShopException {
        Optional<Administrator> administratorOptional = administratorRepository.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (!administratorOptional.isPresent()) {
            Optional<Client> clientOptional = clientRepository.findByLoginAndPassword(request.getLogin(), request.getPassword());
            Client client = clientOptional.get();
            Session session = addCookie(client.getLogin());
            return new RegistrationClientResponse(client.getId(), client.getFirstName(),
                    client.getLastName(), client.getPatronymic(), client.getLogin(), client.getPassword(),
                    session.getToken(), client.getEmail(),  client.getPhoneNumber(), client.getPostalAddress());
        } else {
            Administrator administrator = administratorOptional.get();
            Session session = addCookie(administrator.getLogin());
            return new RegistrationAdminResponse(administrator.getId(),
                    administrator.getFirstName(), administrator.getLastName(), administrator.getPatronymic(),
                    administrator.getLogin(), administrator.getPassword(), session.getToken(), administrator.getPost());
        }
    }

    @Override
    public void logout(String token) throws OnlineShopException {
        sessionRepository.deleteByToken(token);
    }

    @Override
    public SettingsServerResponse getSettingsServer(String cookie) throws OnlineShopException {
        return null;
    }

    @Override
    public void clearDB() throws OnlineShopException {

    }

    public Session addCookie(String login) {
        return sessionRepository.save(new Session(OnlineShopServer.COOKIE,
                UUID.randomUUID().toString(), login));
    }

    public boolean isAdmin(String login){
        return administratorRepository.existsByLogin(login);
    }

    public boolean isClient(String login){
        return clientRepository.existsByLogin(login);
    }

    public String getLogin(String token) {
        return sessionRepository.findByToken(token).get().getLogin();
    }
}
