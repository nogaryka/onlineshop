package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.entity.Administrator;
import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
import net.thumbtack.onlineshop.repository.AdministratorRepository;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static net.thumbtack.onlineshop.config.ConstConfig.COOKIE;

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
    public RegistrationUserResponse login(LoginRequest request) throws OnlineShopExceptionOld {
        Optional<Administrator> administratorOptional = administratorRepository.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (!administratorOptional.isPresent()) {
            Optional<Client> clientOptional = clientRepository.findByLoginAndPassword(request.getLogin(), request.getPassword());
            Client client = clientOptional.get();
            Session session = addCookie(client.getLogin());
            return new RegistrationClientResponse(client.getId(), client.getFirstName(),
                    client.getLastName(), client.getPatronymic(), client.getLogin(), client.getPassword(),
                    session.getToken(), client.getEmail(), client.getPhoneNumber(), client.getPostalAddress(), client.getCash());
        } else {
            Administrator administrator = administratorOptional.get();
            Session session = addCookie(administrator.getLogin());
            return new RegistrationAdminResponse(administrator.getId(),
                    administrator.getFirstName(), administrator.getLastName(), administrator.getPatronymic(),
                    administrator.getLogin(), administrator.getPassword(), session.getToken(), administrator.getPost());
        }
    }

    @Override
    public void logout(String token) throws OnlineShopExceptionOld {
        sessionRepository.deleteByToken(token);
    }

    public Session addCookie(String login) {
        Session session;
        if (!sessionRepository.existsByLogin(login)) {
            session = sessionRepository.save(new Session(COOKIE,
                    UUID.randomUUID().toString(), login));
            return session;
        } else {
            session = sessionRepository.findByLogin(login).get();
            session.setToken(UUID.randomUUID().toString());
            return session;
        }
    }

    public boolean isAdmin(String login) {
        return administratorRepository.existsByLogin(login);
    }

    public boolean isClient(String login) {
        return clientRepository.existsByLogin(login);
    }

    public String getLogin(String token) {
        return sessionRepository.findByToken(token).get().getLogin();
    }
}
