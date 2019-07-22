package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.repository.BasketRepository;
import net.thumbtack.onlineshop.service.BasketService;
import net.thumbtack.onlineshop.service.impl.BasketServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertNotNull;

public class BasketControllerTest {
    private BasketController basketController;

    private BasketService basketService;

    private BasketRepository basketRepository;

  /*  @Before
    public void setUp() {
        basketService = new BasketServiceImpl(basketRepository, productRepository, sessionRepository);
        basketController = new BasketController(basketService);
    }

    @Test
    public void addProductToBasketTest() {
        ResponseEntity<?> response = basketController.addProductToBasket("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void deleteProductFromBasketByIdTest() {
        ResponseEntity<?> response = basketController.deleteProductFromBasketById("test", new MockHttpServletRequest());
        assertNotNull(response);
    }
    @Test
    public void editProductAmountInBasketTest() {
        ResponseEntity<?> response = basketController.editProductAmountInBasket("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void getBasketTest() {
        ResponseEntity<?> response = basketController.getBasket("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void buyProductsToBasketTest() {
        ResponseEntity<?> response = basketController.buyProductsToBasket("test", new MockHttpServletRequest());
        assertNotNull(response);
    }*/


}
