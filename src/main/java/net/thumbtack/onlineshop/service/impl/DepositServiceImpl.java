package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepositServiceImpl implements DepositService {
    private final ClientRepository clientRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public DepositServiceImpl(ClientRepository clientRepository, SessionRepository sessionRepository) {
        this.clientRepository = clientRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public RegistrationClientResponse putMoney(String cookie, DepositRequest request) throws OnlineShopExceptionOld {
        Session session = sessionRepository.findByToken(cookie).get();
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        client.setCash(client.getCash() + request.getMoney());
        clientRepository.save(client);
        return new RegistrationClientResponse(client.getId(), client.getFirstName(),
                client.getLastName(), client.getPatronymic(), client.getLogin(), client.getPassword(),
                cookie, client.getEmail(), client.getPhoneNumber(), client.getPostalAddress(), client.getCash());
    }

    @Override
    public RegistrationClientResponse getMoney(String cookie) throws OnlineShopExceptionOld {
        Session session = sessionRepository.findByToken(cookie).get();
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        return new RegistrationClientResponse(client.getId(), client.getFirstName(),
                client.getLastName(), client.getPatronymic(), client.getLogin(), client.getPassword(),
                cookie, client.getEmail(), client.getPhoneNumber(), client.getPostalAddress(), client.getCash());
    }
}
