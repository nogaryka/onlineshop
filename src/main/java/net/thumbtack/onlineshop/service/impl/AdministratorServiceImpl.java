package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.EditAccountAdminRequest;
import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.entity.Administrator;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
import net.thumbtack.onlineshop.repository.AdministratorRepository;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static net.thumbtack.onlineshop.exceptions.ErrorCod.THIS_LOGIN_IS_EXIST;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    private final AdministratorRepository administratorRepository;
    private final ClientRepository clientRepository;
    private final SessionRepository sessionRepository;
    private final SessionServiceImpl sessionService;

    @Autowired
    public AdministratorServiceImpl(AdministratorRepository administratorRepository, ClientRepository clientRepository,
                                    SessionRepository sessionRepository, SessionServiceImpl sessionService) {
        this.administratorRepository = administratorRepository;
        this.clientRepository = clientRepository;
        this.sessionRepository = sessionRepository;
        this.sessionService = sessionService;
    }

    @Override
    public RegistrationAdminResponse addAdmin(RegistrationAdminRequest request) throws OnlineShopExceptionOld {
        Administrator administrator = new Administrator(request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                request.getLogin(),
                request.getPassword(),
                request.getPost());

        if (clientRepository.existsByLogin(administrator.getLogin())) {
            throw new OnlineShopExceptionOld(THIS_LOGIN_IS_EXIST);
        }
        administratorRepository.save(administrator);
        RegistrationAdminResponse response = (RegistrationAdminResponse) new SessionServiceImpl(administratorRepository,
                clientRepository, sessionRepository).login(new LoginRequest(administrator.getLogin(),
                administrator.getPassword()));
        return response;
    }

    @Override
    public RegistrationAdminResponse editProfileAdmin(String cookie, EditAccountAdminRequest request)
            throws OnlineShopExceptionOld {
        Session session = sessionService.validCookie(cookie);
        Administrator administrator = administratorRepository.findByLogin(session.getLogin()).get();
        administratorRepository.editAdmin(administrator.getId(), request.getFirstName(), request.getLastName(),
                request.getPatronymic(), request.getNewPassword(), request.getPost());
        return new RegistrationAdminResponse(administrator.getId(),
                request.getFirstName(), request.getLastName(), request.getPatronymic(),
                administrator.getLogin(), request.getNewPassword(), cookie, request.getPost());
    }
}
