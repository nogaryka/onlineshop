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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final SessionRepository sessionRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public BasketServiceImpl(BasketRepository basketRepository, ProductRepository productRepository, SessionRepository sessionRepository, ClientRepository clientRepository) {
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.sessionRepository = sessionRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<BuyProductResponse> addProductToBasket(String cookie, BuyProductRequest request) throws OnlineShopExceptionOld {
        Basket basket = new Basket();
        Session session = sessionRepository.findByToken(cookie).get();
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        List<BuyProductResponse> list = new ArrayList<>();
        if (productRepository.existsById(request.getId())) {
            Product product = productRepository.findById(request.getId()).get();
            if (!request.getName().equals(product.getName()) || !request.getPrice().equals(product.getPrice())) {
                throw new OnlineShopExceptionOld();
            }
            Basket.IdClientAndProduct idClientAndProduct = basket.new IdClientAndProduct(client, product);
            basket = new Basket(idClientAndProduct, product.getCount());
            basketRepository.save(basket);
            Iterable<Basket> baskets = basketRepository.findAllByIdClient(client.getId());
            for (Basket basketFromList : baskets) {
                product = productRepository.findById(basketFromList.getIdClientAndProduct().getIdProduct().getId()).get();
                list.add(new BuyProductResponse(product.getId(), product.getName(), product.getPrice(), basketFromList.getAmount()));
            }
        }
        return list;
    }


    @Override
    public void deleteProductFromBasketById(String cookie, Integer id) throws OnlineShopExceptionOld {
        Session session = sessionRepository.findByToken(cookie).get();
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        basketRepository.deleteByIdClientAndIdProduct(client.getId(), id);
    }

    @Override
    public BuyProductResponse editProductAmountInBasket(String cookie, BuyProductRequest request) throws OnlineShopExceptionOld {
        Session session = sessionRepository.findByToken(cookie).get();
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        if (productRepository.existsById(request.getId())) {
            Product product = productRepository.findById(request.getId()).get();
            Basket basket = basketRepository.updateByIdClientAndIdProduct(client.getId(), product.getId(), product.getCount()).get();
        }
        return new BuyProductResponse(request.getId(), request.getName(), request.getPrice(), request.getCount());
    }

    @Override
    public List<BuyProductResponse> getBasket(String cookie) throws OnlineShopExceptionOld {
        Basket basket = new Basket();
        Session session = sessionRepository.findByToken(cookie).get();
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        List<BuyProductResponse> list = new ArrayList<>();
        Iterable<Basket> baskets = basketRepository.findAllByIdClient(client.getId());
        for (Basket basketFromList : baskets) {
            Product product = productRepository.findById(basketFromList.getIdClientAndProduct().getIdProduct().getId()).get();
            list.add(new BuyProductResponse(product.getId(), product.getName(), product.getPrice(), basketFromList.getAmount()));
        }
        return list;
    }

    @Override
    public List<BuyProductResponse> buyProductsToBasket(String cookie, List<BuyProductRequest> request) throws OnlineShopExceptionOld {
        return null;
    }
}
