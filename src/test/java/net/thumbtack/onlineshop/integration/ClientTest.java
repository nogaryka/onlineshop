package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.EditAccountClientRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationClientRequest;
import net.thumbtack.onlineshop.dto.responce.InformarionAdoutClientsForAdminResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.service.DebugService;
import org.junit.After;
import org.junit.Test;
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
import java.util.ArrayList;
import java.util.List;

import static net.thumbtack.onlineshop.config.ConstConfig.COOKIE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ClientTest {
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
    public void clientRegistration() throws Exception {
        RegistrationClientRequest request = new RegistrationClientRequest("Клиент", "Фамилия",
                "Отчество", "ClientLogin", "password123", "client@gmail.com",
                "ClientAddress", "79131533464" );
        MvcResult result = mockMvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(COOKIE))
                .andReturn();
        RegistrationClientResponse response = objectMapper.readValue(result.getResponse().getContentAsString(),RegistrationClientResponse.class);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getEmail(), response.getEmail());
    }

    @Test
    public void getAllClients() throws Exception {
        List<RegistrationClientRequest> requests = new ArrayList<>();
        requests.add(new RegistrationClientRequest("Клиент", "Фамилия", null, "ClientLogin1", "password123", "client1@gmail.com", "ClientAddress", "+79131533461"));
        requests.add(new RegistrationClientRequest("Клиент", "Фамилия", null, "ClientLogin2", "password123", "client2@gmail.com", "ClientAddress", "+79131533462"));
        requests.add(new RegistrationClientRequest("Клиент", "Фамилия", null, "ClientLogin3", "password123", "client3@gmail.com", "ClientAddress", "+79131533463"));
        requests.add(new RegistrationClientRequest("Клиент", "Фамилия", null, "ClientLogin4", "password123", "client4@gmail.com", "ClientAddress", "+79131533464"));
        for (RegistrationClientRequest request : requests) {
            mockMvc.perform(post("/api/clients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(cookie().exists(COOKIE))
                    .andReturn();
        }
        RegistrationAdminRequest request = new RegistrationAdminRequest("Администратор", "Фамилия", null, "Adminlogin", "password", "admin");
        MvcResult registrationResult = mockMvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(COOKIE))
                .andReturn();
        Cookie cookie = registrationResult.getResponse().getCookie(COOKIE);
        assertNotNull(cookie);
        MvcResult clientsResult = mockMvc.perform(get("/api/clients")
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        List<InformarionAdoutClientsForAdminResponse> infoResponse = objectMapper.readValue(clientsResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, InformarionAdoutClientsForAdminResponse.class));
        for (int i = 0; i < 4; i++) {
            assertEquals(requests.get(i).getEmail(), infoResponse.get(i).getEmail());
            assertEquals(requests.get(i).getPostalAddress(), infoResponse.get(i).getPostalAddress());
            assertEquals(requests.get(i).getPhoneNumber().replaceAll("-", ""), infoResponse.get(i).getPhoneNumber());
        }
    }

    @Test
    public void editClientProfile() throws Exception {
        RegistrationClientRequest registrationRequest = new RegistrationClientRequest("Клиент", "Фамилия", null, "ClientLogin1", "password123", "client1@gmail.com", "ClientAddress", "+79131533461");
        MvcResult registrationResult = mockMvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(COOKIE))
                .andReturn();
        Cookie cookie = registrationResult.getResponse().getCookie(COOKIE);
        assertNotNull(cookie);
        EditAccountClientRequest editRequest = new EditAccountClientRequest("КККлиент", "Фамилия", null, "password123", "123password", "client@gmail.com", "ClientAddress", "+84564569895");
        MvcResult editResult = mockMvc.perform(put("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editRequest))
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        RegistrationClientResponse response = objectMapper.readValue(editResult.getResponse().getContentAsString(), RegistrationClientResponse.class);
        assertNotEquals(registrationRequest.getFirstName(), response.getFirstName());
        assertEquals(editRequest.getFirstName(), response.getFirstName());
        assertEquals(editRequest.getPatronymic(), response.getPatronymic());
        assertNotEquals(registrationRequest.getPhoneNumber().replaceAll("[+ -]", ""), response.getPhoneNumber());
    }
}
