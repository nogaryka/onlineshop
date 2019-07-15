package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.dto.request.AdminReportRequest;
import net.thumbtack.onlineshop.dto.request.EditAccountAdminRequest;
import net.thumbtack.onlineshop.dto.request.LoginRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.responce.AdminReportResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.entity.Administrator;
import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.repository.AdministratorRepository;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    private final AdministratorRepository administratorRepository;
    private final ClientRepository clientRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public AdministratorServiceImpl(AdministratorRepository administratorRepository, ClientRepository clientRepository,
                                    SessionRepository sessionRepository) {
        this.administratorRepository = administratorRepository;
        this.clientRepository = clientRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public RegistrationAdminResponse addAdmin(RegistrationAdminRequest request) throws OnlineShopException {
        Administrator administrator = new Administrator(request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                request.getLogin(),
                request.getPassword(),
                request.getPost());

        if(clientRepository.existsByLogin(administrator.getLogin())) {
            throw new OnlineShopException();
        }
        administratorRepository.save(administrator);
        RegistrationAdminResponse response = (RegistrationAdminResponse) new SessionServiceImpl(administratorRepository,
                clientRepository, sessionRepository).login(new LoginRequest(administrator.getLogin(),
                administrator.getPassword()));
        return response;
    }

    @Override
    public RegistrationAdminResponse editProfileAdmin(String cookie, EditAccountAdminRequest request) throws OnlineShopException {
        Administrator administrator = administratorRepository.findByLogin(sessionRepository.findByToken(cookie).get().getLogin()).get();
        administratorRepository.editAdmin(administrator.getId(), request.getFirstName(), request.getLastName(),
                request.getPatronymic(), request.getNewPassword(), request.getPost());
        return new RegistrationAdminResponse(administrator.getId(),
                request.getFirstName(), request.getLastName(), request.getPatronymic(),
                administrator.getLogin(), request.getNewPassword(), cookie, request.getPost());
    }

    @Override
    public AdminReportResponse statement(String cookie, AdminReportRequest request) throws OnlineShopException {
        return null;
    }
}
