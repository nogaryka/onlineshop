package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.DepositRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationClientRequest;
import net.thumbtack.onlineshop.dto.responce.AddProductResponse;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.dto.responce.BuyProductToBasketResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
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
import java.util.ArrayList;
import java.util.List;

import static net.thumbtack.onlineshop.config.ConstConfig.COOKIE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Cookie clientCookie;

    private Cookie adminCookie;

    @Autowired
    private DebugService debugService;

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
        clientCookie = result.getResponse().getCookie(COOKIE);
        assertNotNull(clientCookie);
        RegistrationAdminRequest request2 = new RegistrationAdminRequest("Ivan", "Maksimov",
                "Petrovich", "vsehGlava1", "123456", "Glava");
        MvcResult registrationResult = mockMvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(COOKIE))
                .andReturn();
        adminCookie = registrationResult.getResponse().getCookie(COOKIE);
        assertNotNull(adminCookie);
    }

    private List<AddProductResponse> addProduct() throws Exception {
        List<AddProductResponse> productResponseList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AddProductRequest request = new AddProductRequest(i + "_Product", 100, 10, null);
            MvcResult addProductResult = mockMvc.perform(post("/api/products")
                    .cookie(adminCookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andReturn();
            productResponseList.add(objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class));
        }
        return productResponseList;
    }



    @Test
    public void buyProduct() throws Exception {
        addMoneyToDeposit(500);
        List<AddProductResponse> productResponses = addProduct();
        AddProductResponse product = productResponses.get(0);
        BuyProductRequest request = new BuyProductRequest(product.getId(), product.getName(), product.getPrice(), 3);
        MvcResult result = mockMvc.perform(post("/api/purchases")
                .cookie(clientCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        BuyProductResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), BuyProductResponse.class);
        assertEquals(request.getId(), response.getId());
        MvcResult result2 = mockMvc.perform(get("/api/deposits")
                .cookie(clientCookie))
                .andExpect(status().isOk())
                .andReturn();
        RegistrationClientResponse response2 = objectMapper.readValue(result2.getResponse().getContentAsString(),  RegistrationClientResponse.class);
        assertEquals(200,(long) response2.getCash());
    }

    @Test
    public void buyProduct_withError() throws Exception {
        addMoneyToDeposit(100);
        List<AddProductResponse> productResponses = addProduct();
        AddProductResponse product = productResponses.get(0);
        BuyProductRequest request = new BuyProductRequest(product.getId(), product.getName(), product.getPrice(), 3);
        MvcResult result = mockMvc.perform(post("/api/purchases")
                .cookie(clientCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        OnlineShopExceptionOld response = objectMapper.readValue(result.getResponse().getContentAsString(), OnlineShopExceptionOld.class);
        assertEquals("У вас недостаточно средств на счете", response.getMessage());
    }

    @Test
    public void buyProductsFromBasket() throws Exception {
        addMoneyToDeposit(1000);
        List<AddProductResponse> productResponses = addProduct();
        List<BuyProductRequest> productRequest = new ArrayList<>();
        for (AddProductResponse product : productResponses) {
            productRequest.add(new BuyProductRequest(product.getId(), product.getName(), product.getPrice(), 3));
        }
        for (BuyProductRequest request : productRequest) {
            mockMvc.perform(post("/api/baskets")
                    .cookie(clientCookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
        MvcResult buyResult = mockMvc.perform(post("/api/purchases/baskets")
                .cookie(clientCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andReturn();
        BuyProductToBasketResponse response =  objectMapper.readValue(buyResult.getResponse().getContentAsString(), BuyProductToBasketResponse.class);
        System.out.println(response);
        assertEquals(3, response.getBought().size());
        assertTrue(response.getRemaining().isEmpty());
    }

    private void addMoneyToDeposit(int money) throws Exception {
        DepositRequest request = new DepositRequest(money);
        MvcResult result = mockMvc.perform(put("/api/deposits")
                .cookie(clientCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        RegistrationClientResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), RegistrationClientResponse.class);
        assertEquals(request.getMoney(), response.getCash());
    }
}
