package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.EditAccountAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.service.DebugService;
import org.junit.After;
import org.junit.Test;

import static net.thumbtack.onlineshop.config.ConstConfig.COOKIE;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdministratorTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DebugService debugService;

    @After
    public void clearDB() {
        debugService.clearDB();
    }

    @Test
    public void addAdminTest() throws Exception {
        RegistrationAdminRequest request = new RegistrationAdminRequest("Ivan", "Maksimov",
                "Petrovich", "vsehGlava1", "123456", "Glava");
        MvcResult result = mockMvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(COOKIE))
                .andReturn();
        RegistrationAdminResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),
                RegistrationAdminResponse.class);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getLogin(), response.getLogin());
        assertEquals(request.getPassword(), response.getPassword());
        assertEquals(request.getPost(), response.getPost());
    }

    @Test
    public void editAdminProfile() throws Exception {
        RegistrationAdminRequest registrationRequest = new RegistrationAdminRequest("Администратор", "Фамилия", null, "admin", "Adminlogin", "password");
        MvcResult registrationResult = mockMvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(COOKIE))
                .andReturn();
        Cookie cookie = registrationResult.getResponse().getCookie(COOKIE);
        assertNotNull(cookie);
        EditAccountAdminRequest editRequest = new EditAccountAdminRequest("АААААдминистратор", "Фамилия", null, "Adminlogin", "password", "password123");
        MvcResult editResult = mockMvc.perform(put("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editRequest))
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        RegistrationAdminResponse response = objectMapper.readValue(editResult.getResponse().getContentAsString(),  RegistrationAdminResponse.class);
        assertNotEquals(registrationRequest.getFirstName(), response.getFirstName());
        assertEquals(editRequest.getFirstName(), response.getFirstName());
        assertEquals(editRequest.getPatronymic(), response.getPatronymic());
    }
}
