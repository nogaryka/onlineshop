package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.entity.Product;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.ProductRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final ProductRepository productRepository;
    private final SessionRepository sessionRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public PurchaseServiceImpl(ProductRepository productRepository, SessionRepository sessionRepository, ClientRepository clientRepository) {
        this.productRepository = productRepository;
        this.sessionRepository = sessionRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public BuyProductResponse buyProduct(String cookie, BuyProductRequest request) throws OnlineShopExceptionOld {
        Session session = sessionRepository.findByToken(cookie).get();
        Client client = clientRepository.findByLogin(session.getLogin()).get();
        if(productRepository.existsById(request.getId())) {
            Product product = productRepository.findById(request.getId()).get();
            if (product.getCount() < request.getCount()) {
                throw new OnlineShopExceptionOld();
            } else if(client.getCash() < request.getCount() * request.getPrice()) {
                throw new OnlineShopExceptionOld();
            } else if(!request.getName().equals(product.getName()) || !request.getPrice().equals(product.getPrice())) {
                throw new OnlineShopExceptionOld();
            }
            product.setCount(product.getCount() - request.getCount());
            productRepository.save(product);
            client.setCash(client.getCash() - request.getCount() * request.getPrice());
            clientRepository.save(client);
            return new BuyProductResponse(request.getId(), request.getName(), request.getPrice(), request.getCount());
        }
        return null;
    }
}
