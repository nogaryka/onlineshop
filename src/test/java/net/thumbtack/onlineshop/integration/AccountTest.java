package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.RegistrationClientRequest;
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

import static net.thumbtack.onlineshop.config.ConstConfig.COOKIE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AccountTest {
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
    public void getAccountsInfo() throws Exception {
        RegistrationClientRequest registrationRequest = new RegistrationClientRequest("Клиент",
                "Фамилия", "Отчество", "ClientLogin" , "password123",
                "client@gmail.com", "Client_Address", "79131533464");
        MvcResult registrationResult = mockMvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(COOKIE))
                .andReturn();
        Cookie cookie = registrationResult.getResponse().getCookie(COOKIE);
        assertNotNull(cookie);
        RegistrationClientResponse registrationResponse = objectMapper.readValue(registrationResult.getResponse().getContentAsString(),RegistrationClientResponse.class);
        MvcResult infoResult = mockMvc.perform(get("/api/accounts")
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        RegistrationClientResponse infoResponse = objectMapper.readValue(infoResult.getResponse().getContentAsString(), RegistrationClientResponse.class);
        assertEquals(registrationResponse, infoResponse);
    }
}
