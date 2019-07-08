package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.AdminReportRequest;
import net.thumbtack.onlineshop.dto.request.EditAccountAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationUserRequest;
import net.thumbtack.onlineshop.dto.request.SessionCookieRequest;
import net.thumbtack.onlineshop.dto.responce.AdminReportResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

public interface AdministratorService {
    RegistrationUserResponse addAdmin(RegistrationUserRequest request) throws OnlineShopException;

    RegistrationUserResponse editProfileAdmin(SessionCookieRequest cookie, EditAccountAdminRequest request) throws OnlineShopException;

    AdminReportResponse statement(String cookie, AdminReportRequest request) throws OnlineShopException;
}

