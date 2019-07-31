package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Basket;
import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.entity.Product;
import net.thumbtack.onlineshop.service.DebugService;
import org.apache.commons.collections4.IterableUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BasketRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ClientRepository clientRepository;

    Client client;
    private List<Product> products;

    @Autowired
    private DebugService debugService;

    @After
    public void clearDB() {
        debugService.clearDB();
    }

    @Before
    public void clear() {
        client = new Client("userTest", "last_nameTest", null, "loginTest", "passwordTest", "box_Test@yandex.com", "11111111111", "address");
        client.setCash(1000);
        client = clientRepository.save(client);
        assertTrue(client.getId() != 0);
        products = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            products.add(new Product("prod_" + i, 10, 1000));
        }
        for (Product product : products) {
            product = productRepository.save(product);
            assertTrue(product.getId() != 0);
        }
    }

    @Test
    public void testAddProductToBasketAndGetBasket() {
        Basket.IdClientAndProduct idClientAndProduct = new Basket.IdClientAndProduct(client, products.get(0));
        Basket basket = new Basket(idClientAndProduct, 10);
        basketRepository.save(basket);
        idClientAndProduct = new Basket.IdClientAndProduct(client, products.get(1));
        basket = new Basket(idClientAndProduct, 2);
        basketRepository.save(basket);
        idClientAndProduct = new Basket.IdClientAndProduct(client, products.get(2));
        basket = new Basket(idClientAndProduct, 1);
        basketRepository.save(basket);
        idClientAndProduct = new Basket.IdClientAndProduct(client, products.get(3));
        basket = new Basket(idClientAndProduct, 5);
        basketRepository.save(basket);
        List<Basket> basketItems = IterableUtils.toList(basketRepository.findAllByIdClient(client.getId()));
        assertFalse(basketItems.isEmpty());
    }

    @Test
    public void buyProduct() {
        client = clientRepository.findById(client.getId()).get();
        Product product = productRepository.findById(products.get(0).getId()).get();
        int count = 10;
        client.setCash(client.getCash() - product.getPrice()*count);
        product.setCount(product.getCount() - count);
        clientRepository.save(client);
        productRepository.save(product);
        Client clientAfterPurchase = clientRepository.findById(client.getId()).get();
        Product productAfterPurchase = productRepository.findById(product.getId()).get();
        assertEquals(900, (long) clientAfterPurchase.getCash());
        assertEquals(990, (long) productAfterPurchase.getCount());
    }

    @Test
    public void deleteProductWithRefKey() {
        client = clientRepository.findById(client.getId()).get();
        Product product = productRepository.findById(products.get(0).getId()).get();
        Product product2 = productRepository.findById(products.get(1).getId()).get();
        Basket.IdClientAndProduct idClientAndProduct = new Basket.IdClientAndProduct(client, product);
        Basket basket = new Basket(idClientAndProduct, 100);
        basketRepository.save(basket);
        idClientAndProduct = new Basket.IdClientAndProduct(client, product2);
        basket = new Basket(idClientAndProduct, 100);
        basketRepository.save(basket);
        idClientAndProduct = new Basket.IdClientAndProduct(client, product);
        basket = new Basket(idClientAndProduct, 10000);
        basketRepository.save(basket);
        idClientAndProduct = new Basket.IdClientAndProduct(client, product2);
        basket = new Basket(idClientAndProduct, 20000);
        basketRepository.save(basket);
        List<Basket> basketItems = IterableUtils.toList(basketRepository.findAllByIdClient(client.getId()));;
        List<Basket> basketItemsAfterDelete = IterableUtils.toList(basketRepository.findAllByIdClient(client.getId()));

        assertEquals(2, basketItems.size());
        assertEquals(10000, (long) basketItems.get(0).getAmount());
        assertEquals(20000, (long) basketItems.get(1).getAmount());

        assertEquals(2, basketItemsAfterDelete.size());
        assertEquals(10000, (long) basketItemsAfterDelete.get(0).getAmount());
        ;
        assertEquals(20000, (long) basketItemsAfterDelete.get(1).getAmount());
    }

    @Test
    public void testGetBasketByClient() {
        List<Basket> basketItems = IterableUtils.toList(basketRepository.findAllByIdClient(client.getId()));
        if (basketItems == null) {
            fail();
        }
        Basket.IdClientAndProduct idClientAndProduct = new Basket.IdClientAndProduct(client, products.get(0));
        Basket basket = new Basket(idClientAndProduct, 10);
        basketRepository.save(basket);
        basketItems = IterableUtils.toList(basketRepository.findAllByIdClient(client.getId()));
        assertEquals(1, basketItems.size());
        assertEquals(products.get(0).getName(), basketItems.get(0).getIdClientAndProduct().getIdProduct().getName());
    }

    @Test
    public void changeProductToBasket() {
        Basket.IdClientAndProduct idClientAndProduct = new Basket.IdClientAndProduct(client, products.get(0));
        Basket basket = new Basket(idClientAndProduct, 10);
        basketRepository.save(basket);
        basket.setAmount(100);
        basketRepository.save(basket);
        List<Basket> basketItems =  IterableUtils.toList(basketRepository.findAllByIdClient(client.getId()));
        assertFalse(basketItems.isEmpty());
        assertEquals(100,(long) basketItems.get(0).getAmount());
    }

    @Test
    public void deleteProductFromBasket() {
        Basket.IdClientAndProduct idClientAndProduct = new Basket.IdClientAndProduct(client, products.get(0));
        Basket basket = new Basket(idClientAndProduct, 10);
        basketRepository.save(basket);
        List<Basket> basketItems =IterableUtils.toList(basketRepository.findAllByIdClient(client.getId()));
        assertFalse(basketItems.isEmpty());
        basketItems = IterableUtils.toList(basketRepository.findAllByIdClient(client.getId()));
        assertFalse(basketItems.isEmpty());
    }

}
