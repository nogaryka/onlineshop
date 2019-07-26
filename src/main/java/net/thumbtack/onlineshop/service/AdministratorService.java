package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.EditAccountAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;

public interface AdministratorService {
    RegistrationAdminResponse addAdmin(RegistrationAdminRequest request) throws OnlineShopExceptionOld;

    RegistrationAdminResponse editProfileAdmin(String cookie, EditAccountAdminRequest request) throws OnlineShopExceptionOld;

   // AdminReportResponse statement(String cookie, AdminReportRequest request) throws OnlineShopExceptionOld;
}

