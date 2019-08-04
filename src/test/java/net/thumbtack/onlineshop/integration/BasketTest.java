package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationClientRequest;
import net.thumbtack.onlineshop.dto.responce.AddProductResponse;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
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
import static net.thumbtack.onlineshop.exceptions.ErrorCod.INCORRECT_PRODUCT_ID_IN_BASKET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BasketTest {
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
    public void addProductToBasket() throws Exception {
        List<AddProductResponse> productResponses = addProduct();
        AddProductResponse product = productResponses.get(0);
        BuyProductRequest request = new BuyProductRequest(product.getId(), product.getName(), product.getPrice(), 100000);
        MvcResult result = mockMvc.perform(post("/api/baskets")
                .cookie(clientCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        List<BuyProductResponse> responses = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, BuyProductResponse.class));
        assertEquals(product.getId(), responses.get(0).getId());
        assertEquals(100000,(long) responses.get(0).getCount());
        assertNotEquals(product.getCount(), responses.get(0).getCount());
    }

    @Test
    public void addNonexistentProductToBasket() throws Exception {
        BuyProductRequest request = new BuyProductRequest(15641, "SomeProd", 1, 100000);
        MvcResult result = mockMvc.perform(post("/api/baskets")
                .cookie(clientCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        OnlineShopExceptionOld response = objectMapper.readValue(result.getResponse().getContentAsString(), OnlineShopExceptionOld.class);
        assertEquals(INCORRECT_PRODUCT_ID_IN_BASKET, response.getMessage());
    }

    @Test
    public void deleteProductFromBasket() throws Exception {
        List<AddProductResponse> productResponses = addProduct();
        AddProductResponse product = productResponses.get(0);
        BuyProductRequest request = new BuyProductRequest(product.getId(), product.getName(), product.getPrice(), 100000);
        MvcResult result = mockMvc.perform(post("/api/baskets")
                .cookie(clientCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        List<BuyProductResponse> responses = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, BuyProductResponse.class));
        assertEquals(product.getId(), responses.get(0).getId());
        assertEquals(100000, (long) responses.get(0).getCount());
        assertNotEquals(product.getCount(), responses.get(0).getCount());
        MvcResult deleteResult = mockMvc.perform(delete("/api/baskets/{id}", responses.get(0).getId())
                .cookie(clientCookie))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("", deleteResult.getResponse().getContentAsString());
    }

    @Test
    public void changeProductCount() throws Exception {
        List<AddProductResponse> productResponses = addProduct();
        AddProductResponse product = productResponses.get(0);
        BuyProductRequest request = new BuyProductRequest(product.getId(), product.getName(), product.getPrice(), 100000);
        MvcResult result = mockMvc.perform(post("/api/baskets")
                .cookie(clientCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        List<BuyProductResponse> responses = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, BuyProductResponse.class));
        assertEquals(product.getId(), responses.get(0).getId());
        assertEquals(100000, (long) responses.get(0).getCount());
        assertNotEquals(product.getCount(), responses.get(0).getCount());
        BuyProductRequest productRequest = new BuyProductRequest(product.getId(), product.getName(), product.getPrice(), 10);
        MvcResult changeResult = mockMvc.perform(put("/api/baskets")
                .cookie(clientCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andReturn();
        List<BuyProductResponse> newResponses = objectMapper.readValue(changeResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, BuyProductResponse.class));
        assertEquals(10, (long) newResponses.get(0).getCount());
        assertEquals(product.getId(), newResponses.get(0).getId());
    }

    @Test
    public void getProductsFromBasket() throws Exception {
        List<AddProductResponse> productResponses = addProduct();
        for (int i = 0; i < 3; i++) {
            AddProductResponse product = productResponses.get(i);
            BuyProductRequest request = new BuyProductRequest(product.getId(), product.getName(), product.getPrice(), 100000);
            MvcResult result = mockMvc.perform(post("/api/baskets")
                    .cookie(clientCookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andReturn();
            List<BuyProductResponse> responses = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, BuyProductResponse.class));
            assertEquals(product.getId(), responses.get(i).getId());
            assertEquals((long) request.getCount(), (long) responses.get(i).getCount());
        }
        MvcResult result = mockMvc.perform(get("/api/baskets")
                .cookie(clientCookie))
                .andExpect(status().isOk())
                .andReturn();
        List<BuyProductResponse> responses = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, BuyProductResponse.class));
        assertEquals(3, responses.size());
        for (int i = 0; i < 3; i++) {
            assertEquals(productResponses.get(i).getId(), responses.get(i).getId());
        }
    }
}
