package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.service.ClientService;
import net.thumbtack.onlineshop.service.impl.ClientServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertNotNull;

public class ClientControllerTest {
    private ClientController clientController;

    private ClientService clientService;

    private ClientRepository clientRepository;

    /*@Before
    public void setUp() {
        clientService = new ClientServiceImpl(clientRepository);
        clientController = new ClientController(clientService);
    }

    @Test
    public void addClientTest() {
        ResponseEntity<?> response = clientController.addClient("test", new MockHttpServletRequest());
        assertNotNull(response);
    }*/

   /* @Test
    public void editProfileClientTest() {
        ResponseEntity<?> response = clientController.editProfileClient("test", new MockHttpServletRequest());
        assertNotNull(response);
    }*/

    /*@Test
    public void getInfoAboutClientForAdminTest() {
        ResponseEntity<?> response = clientController.getInfoAboutClientsForAdmin("test", new MockHttpServletRequest());
        assertNotNull(response);
    }*/

    @Test
    public void putMoneyTest() {
        ResponseEntity<?> response = clientController.putMoney("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void getMoneyTest() {
        ResponseEntity<?> response = clientController.getMoney("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void buyProductTest() {
        ResponseEntity<?> response = clientController.buyProduct("test", new MockHttpServletRequest());
        assertNotNull(response);
    }
}