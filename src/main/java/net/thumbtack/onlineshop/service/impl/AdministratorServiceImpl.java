package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.AdminReportRequest;
import net.thumbtack.onlineshop.dto.request.EditAccountAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationUserRequest;
import net.thumbtack.onlineshop.dto.request.SessionCookieRequest;
import net.thumbtack.onlineshop.dto.responce.AdminReportResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
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
    public RegistrationUserResponse addAdmin(RegistrationUserRequest request) throws OnlineShopException {
        return null;
    }

    @Override
    public RegistrationUserResponse editProfileAdmin(SessionCookieRequest cookie, EditAccountAdminRequest request) throws OnlineShopException {
        return null;
    }

    @Override
    public AdminReportResponse statement(String cookie, AdminReportRequest request) throws OnlineShopException {
        return null;
    }
}
