package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.repository.AdministratorRepository;
import net.thumbtack.onlineshop.repository.BasketRepository;
import net.thumbtack.onlineshop.repository.CategoryRepository;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.ProductRepository;
import net.thumbtack.onlineshop.repository.PurchaseRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.DebugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebugServiceImpl implements DebugService {
    private final SessionRepository sessionRepository;
    private final AdministratorRepository administratorRepository;
    private final ClientRepository clientRepository;
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PurchaseRepository purchaseRepository;

    @Autowired
    public DebugServiceImpl(SessionRepository sessionRepository, AdministratorRepository administratorRepository,
                            ClientRepository clientRepository, BasketRepository basketRepository,
                            ProductRepository productRepository, CategoryRepository categoryRepository, PurchaseRepository purchaseRepository) {
        this.sessionRepository = sessionRepository;
        this.administratorRepository = administratorRepository;
        this.clientRepository = clientRepository;
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public void clearDB() {
        sessionRepository.deleteAll();
        purchaseRepository.deleteAll();
        basketRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        clientRepository.deleteAll();
        administratorRepository.deleteAll();
    }
}
