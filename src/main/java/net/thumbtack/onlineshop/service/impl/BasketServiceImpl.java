package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.entity.Basket;
import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.entity.Product;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
import net.thumbtack.onlineshop.repository.BasketRepository;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.ProductRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.BasketService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static net.thumbtack.onlineshop.exceptions.ErrorCod.INCORRECT_COUNT;
import static net.thumbtack.onlineshop.exceptions.ErrorCod.INCORRECT_PRICE_OR_NAME;
import static net.thumbtack.onlineshop.exceptions.ErrorCod.INCORRECT_PRODUCT_ID;

@Service
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final SessionRepository sessionRepository;
    private final ClientRepository clientRepository;
    private final SessionServiceImpl sessionService;

    @Autowired
    public BasketServiceImpl(BasketRepository basketRepository, ProductRepository productRepository,
                             SessionRepository sessionRepository,
                             ClientRepository clientRepository,
                             SessionServiceImpl sessionService) {
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.sessionRepository = sessionRepository;
        this.clientRepository = clientRepository;
        this.sessionService = sessionService;
    }

    @Override
    public List<BuyProductResponse> addProductToBasket(String cookie, BuyProductRequest request)
            throws OnlineShopExceptionOld {
        Session session = sessionService.validCookie(cookie);
        Basket basket;
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        List<BuyProductResponse> list = new ArrayList<>();
        if (productRepository.existsById(request.getId())) {
            Product product = productRepository.findById(request.getId()).get();
            if (!request.getName().equals(product.getName()) || !request.getPrice().equals(product.getPrice())) {
                throw new OnlineShopExceptionOld(INCORRECT_PRICE_OR_NAME);
            }
            if(request.getCount() == null || request.getCount() == 0) {
                request.setCount(1);
            }
            Basket.IdClientAndProduct idClientAndProduct = new Basket.IdClientAndProduct(client, product);
            basket = new Basket(idClientAndProduct, request.getCount());
            basketRepository.save(basket);
            Iterable<Basket> baskets = basketRepository.findAllByIdClient(client.getId());
            for (Basket basketFromList : baskets) {
                product = productRepository.findById(basketFromList.getIdClientAndProduct().getIdProduct().getId()).get();
                list.add(new BuyProductResponse(product.getId(), product.getName(), product.getPrice(), basketFromList.getAmount()));
            }
        } else {
            throw new OnlineShopExceptionOld(INCORRECT_PRODUCT_ID);
        }
        return list;
    }


    @Override
    public void deleteProductFromBasketById(String cookie, Integer id) throws OnlineShopExceptionOld {
        Session session = sessionService.validCookie(cookie);
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        basketRepository.deleteByIdClientAndIdProduct(client.getId(), id);
    }

    @Override
    public List<BuyProductResponse> editProductAmountInBasket(String cookie, BuyProductRequest request) throws OnlineShopExceptionOld {
        Session session = sessionService.validCookie(cookie);
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        List<BuyProductResponse> list = new ArrayList<>();
        if (productRepository.existsById(request.getId())) {
            Product product = productRepository.findById(request.getId()).get();
            if (!request.getName().equals(product.getName()) || !request.getPrice().equals(product.getPrice())) {
                throw new OnlineShopExceptionOld(INCORRECT_PRICE_OR_NAME);
            }
            if(request.getCount() == null || request.getCount() == 0) {
                throw new OnlineShopExceptionOld(INCORRECT_COUNT);
            }
            basketRepository.updateByIdClientAndIdProduct(client.getId(), product.getId(), request.getCount());
            List<Basket> baskets = IterableUtils.toList(basketRepository.findAll());
            for (Basket basket : baskets) {
                list.add(new BuyProductResponse(basket.getIdClientAndProduct().getIdProduct().getId(),
                        basket.getIdClientAndProduct().getIdProduct().getName(),
                        basket.getIdClientAndProduct().getIdProduct().getPrice(),
                        basket.getAmount()));
            }
        }
        return list;
    }

    @Override
    public List<BuyProductResponse> getBasket(String cookie) throws OnlineShopExceptionOld {
        Basket basket = new Basket();
        Session session = sessionService.validCookie(cookie);
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        List<BuyProductResponse> list = new ArrayList<>();
        Iterable<Basket> baskets = basketRepository.findAllByIdClient(client.getId());
        for (Basket basketFromList : baskets) {
            Product product = productRepository.findById(basketFromList.getIdClientAndProduct().getIdProduct().getId()).get();
            list.add(new BuyProductResponse(product.getId(), product.getName(), product.getPrice(), basketFromList.getAmount()));
        }
        return list;
    }
}
