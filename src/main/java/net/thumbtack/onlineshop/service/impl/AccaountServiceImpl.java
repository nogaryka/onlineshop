package net.thumbtack.onlineshop.service.impl;

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
import net.thumbtack.onlineshop.service.AccaountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccaountServiceImpl implements AccaountService {
    private final SessionRepository sessionRepository;
    private final AdministratorRepository administratorRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public AccaountServiceImpl(SessionRepository sessionRepository, AdministratorRepository administratorRepository,
                               ClientRepository clientRepository) {
        this.sessionRepository = sessionRepository;
        this.administratorRepository = administratorRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public RegistrationUserResponse getInfoAboutMe(String cookie) throws OnlineShopException {
       Session session = sessionRepository.findByToken(cookie).get();
       SessionServiceImpl sessionService = new SessionServiceImpl(administratorRepository, clientRepository,
               sessionRepository);
       if(sessionService.isAdmin(session.getLogin())){
           Administrator administrator = administratorRepository.findByLogin(session.getLogin()).get();
           return new RegistrationAdminResponse(administrator.getId(),
                   administrator.getFirstName(), administrator.getLastName(), administrator.getPatronymic(),
                   administrator.getLogin(), administrator.getPassword(), session.getToken(), administrator.getPost());
       }
       else if (sessionService.isClient(session.getLogin())) {
           Client client = clientRepository.findByLogin(session.getLogin()).get();
           return new RegistrationClientResponse(client.getId(), client.getFirstName(),
                   client.getLastName(), client.getPatronymic(), client.getLogin(), client.getPassword(),
                   session.getToken(), client.getEmail(),  client.getPhoneNumber(), client.getPostalAddress());
       }
       return null;
    }
}
