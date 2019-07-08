package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.service.SessionService;
import net.thumbtack.onlineshop.service.impl.SessionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertNotNull;

public class SessionControllerTest {
    private SessionController sessionController;
    private SessionService sessionService;

    @Before
    public void setUp() {
        sessionService = new SessionServiceImpl();
        sessionController = new SessionController(sessionService);
    }

    @Test
    public void loginTest() {
        ResponseEntity<?> response = sessionController.login("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void logoutTest() {
        ResponseEntity<?> response = sessionController.logout("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void getInfoAboutMeTest() {
        ResponseEntity<?> response = sessionController.getInfoAboutMe("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void getSettingsServerTest() {
        ResponseEntity<?> response = sessionController.getSettingsServer("test", new MockHttpServletRequest());
        assertNotNull(response);
    }

    @Test
    public void clearDBTest() {
        ResponseEntity<?> response = sessionController.clearDB("test", new MockHttpServletRequest());
        assertNotNull(response);
    }
}
