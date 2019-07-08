package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.repository.AdministratorRepository;
import net.thumbtack.onlineshop.service.AdministratorService;
import net.thumbtack.onlineshop.service.impl.AdministratorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

public class AdministratorControllerTest {
    private AdministratorController administratorController;
    private AdministratorService administratorService;
    private AdministratorRepository administratorRepository;

    @Before
    public void setUp() {
        administratorService = new AdministratorServiceImpl(administratorRepository);
        administratorController = new AdministratorController(administratorService);
    }

    @Test
    public void addAdminTest() {
        ResponseEntity<?> response = administratorController.addAdmin("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void editProfileAdminTest() {
        ResponseEntity<?> response = administratorController.editProfileAdmin("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void statementTest() {
        ResponseEntity<?> response = administratorController.statement("test", new MockHttpServletRequest());
        assertNotNull(response);
    }
}