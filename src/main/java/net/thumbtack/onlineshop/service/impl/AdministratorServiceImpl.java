package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.AdminReportRequest;
import net.thumbtack.onlineshop.dto.request.EditAccountAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.responce.AdminReportResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.entity.Administrator;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.repository.AdministratorRepository;
import net.thumbtack.onlineshop.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    private final AdministratorRepository administratorRepository;

    @Autowired
    public AdministratorServiceImpl(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    @Override
    public RegistrationAdminResponse addAdmin(RegistrationAdminRequest request) throws OnlineShopException {
        Administrator administrator = new Administrator(request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                request.getLogin(),
                request.getPassword(),
                request.getPost());

        administratorRepository.save(administrator);

        RegistrationAdminResponse response = new RegistrationAdminResponse(administrator.getId(),
                administrator.getFirstName(),
                administrator.getLastName(),
                administrator.getPatronymic(),
                administrator.getLogin(),
                administrator.getPassword(),
                administrator.getPost());
        return response;
    }

    @Override
    public RegistrationAdminResponse editProfileAdmin(String cookie, EditAccountAdminRequest request) throws OnlineShopException {
        return null;
    }

    @Override
    public AdminReportResponse statement(String cookie, AdminReportRequest request) throws OnlineShopException {
        return null;
    }
}
