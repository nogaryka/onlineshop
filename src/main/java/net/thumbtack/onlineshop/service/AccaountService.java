package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;

public interface AccaountService {
    RegistrationUserResponse getInfoAboutMe(String cookie) throws OnlineShopExceptionOld;
}
