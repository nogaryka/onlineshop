package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.AdminReportRequest;
import net.thumbtack.onlineshop.dto.request.EditAccountAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.responce.AdminReportResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;

public interface AdministratorService {
    RegistrationAdminResponse addAdmin(RegistrationAdminRequest request) throws OnlineShopException;

    RegistrationAdminResponse editProfileAdmin(String cookie, EditAccountAdminRequest request) throws OnlineShopException;

    AdminReportResponse statement(String cookie, AdminReportRequest request) throws OnlineShopException;
}

