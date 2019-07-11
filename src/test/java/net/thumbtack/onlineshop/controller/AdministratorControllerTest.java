package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationUserRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.repository.AdministratorRepository;
import net.thumbtack.onlineshop.service.AdministratorService;
import net.thumbtack.onlineshop.service.impl.AdministratorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdministratorControllerTest {
    private AdministratorController administratorController;

    private AdministratorService administratorService;

    private AdministratorRepository administratorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        administratorService = new AdministratorServiceImpl(administratorRepository);
        administratorController = new AdministratorController(administratorService);
    }

    @Test
    public void addAdminTest() throws Exception {
        RegistrationAdminRequest request = new RegistrationAdminRequest("Ivan", "Maksimov",
                "Petrovich", "vsehGlava1", "123456", "Glava");
        MvcResult result = mockMvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(OnlineShopServer.COOKIE))
                .andReturn();
        RegistrationAdminResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                RegistrationAdminResponse.class);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPost(), response.getPost());
    }


   /* @Test
    public void editProfileAdminTest() {
        ResponseEntity<?> response = administratorController.editProfileAdmin("test", new MockHttpServletRequest());
        assertNotNull(response);
    }*/

    @Test
    public void statementTest() {
        ResponseEntity<?> response = administratorController.statement("test", new MockHttpServletRequest());
        assertNotNull(response);
    }
}