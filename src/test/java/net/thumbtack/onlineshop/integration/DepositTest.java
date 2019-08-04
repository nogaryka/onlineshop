package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationClientRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.service.DebugService;
import org.junit.After;
import org.junit.Before;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DepositTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DebugService debugService;

    private Cookie cookie;

    @After
    public void clearDB() {
        debugService.clearDB();
    }

    @Before
    public void before() throws Exception {
        RegistrationClientRequest request = new RegistrationClientRequest("Клиент",
                "Фамилия", "Отчество", "ClientLogin" , "password123",
                "client@gmail.com", "Client_Address", "79131533464");
        MvcResult result = mockMvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(COOKIE))
                .andReturn();
        RegistrationClientResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), RegistrationClientResponse.class);
        cookie = result.getResponse().getCookie(COOKIE);
        assertNotNull(cookie);
    }

    @Test
    public void addMoney() throws Exception {
        DepositRequest request = new DepositRequest(100);
        MvcResult result = mockMvc.perform(put("/api/deposits")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        RegistrationClientResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), RegistrationClientResponse.class);
        assertEquals(request.getMoney(), response.getCash());
    }

    @Test
    public void getDeposit() throws Exception {
        DepositRequest request = new DepositRequest(100);
        MvcResult result = mockMvc.perform(put("/api/deposits")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        RegistrationClientResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), RegistrationClientResponse.class);
        assertEquals(request.getMoney(), response.getCash());
        MvcResult result2 = mockMvc.perform(get("/api/deposits")
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        RegistrationClientResponse response2 = objectMapper.readValue(result2.getResponse().getContentAsString(),  RegistrationClientResponse.class);
        assertEquals(response.getCash(), response2.getCash());
    }
}
