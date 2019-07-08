package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.SessionCookieRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.repository.BasketRepository;
import net.thumbtack.onlineshop.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;

    @Autowired
    public BasketServiceImpl(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    public BuyProductResponse addProductToBasket(SessionCookieRequest cookie, BuyProductRequest request) throws OnlineShopException {
        return null;
    }

    @Override
    public void deleteProductFromBasketById(SessionCookieRequest cookie, Integer id) throws OnlineShopException {

    }

    @Override
    public BuyProductResponse editProductAmountInBasket(SessionCookieRequest cookie, BuyProductRequest request) throws OnlineShopException {
        return null;
    }

    @Override
    public List<BuyProductResponse> getBasket(SessionCookieRequest cookie) throws OnlineShopException {
        return null;
    }

    @Override
    public List<BuyProductResponse> buyProductsToBasket(SessionCookieRequest cookie, List<BuyProductRequest> request) throws OnlineShopException {
        return null;
    }
}
