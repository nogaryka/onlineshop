package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationClientRequest;
import net.thumbtack.onlineshop.dto.responce.AddProductResponse;
import net.thumbtack.onlineshop.dto.responce.BuyProductResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BasketServiceTest {
    @Autowired
    private ClientService clientService;

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BasketService basketService;

    @Autowired
    private DebugService debugService;

    private String adminCookie;

    private String clientCookie;

    @After
    public void before() {
        debugService.clearDB();
    }

    @Before
    public void setUp() {
        RegistrationAdminRequest request2 = new RegistrationAdminRequest("Ivan", "Maksimov",
                "Petrovich", "vsehGlava1", "123456", "Glava");
        RegistrationAdminResponse response2 = administratorService.addAdmin(request2);
        adminCookie = response2.getToken();
        RegistrationClientRequest request = new RegistrationClientRequest("Клиент",
                "Фамилия", "Отчество", "ClientLogin", "password123",
                "client@gmail.com", "Client_Address", "79131533464");
        RegistrationClientResponse response = clientService.addClient(request);
        clientCookie = response.getToken();
    }

    private List<AddProductResponse> addProduct() {
        List<AddProductResponse> productResponseList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AddProductRequest request = new AddProductRequest(i + "_Product", 100, 10, null);
            AddProductResponse response = productService.addProduct(adminCookie, request);
            productResponseList.add(response);
        }
        return productResponseList;
    }

    @Test
    public void addProductToBasketTest() {
        List<AddProductResponse> productResponseList = addProduct();
        BuyProductRequest request;
        for (int i = 0; i < productResponseList.size() - 1; i++) {
            request = new BuyProductRequest(productResponseList.get(i).getId(), productResponseList.get(i).getName(),
                    productResponseList.get(i).getPrice(), productResponseList.get(i).getCount() - i);
            List<BuyProductResponse> productResponse = basketService.addProductToBasket(clientCookie, request);
            assertEquals(i + 1, productResponse.size());
            assertEquals(productResponseList.get(i).getId(), productResponse.get(i).getId());
            assertEquals(productResponseList.get(i).getName(), productResponse.get(i).getName());
            assertEquals(productResponseList.get(i).getPrice(), productResponse.get(i).getPrice());
            assertEquals(productResponseList.get(i).getCount() - i, (long) productResponse.get(i).getCount());
        }
    }

    @Test(expected = OnlineShopExceptionOld.class)
    public void addProductToBasketTestError_Price() {
        List<AddProductResponse> productResponseList = addProduct();
        BuyProductRequest request;
        request = new BuyProductRequest(productResponseList.get(0).getId(), productResponseList.get(0).getName(),
                productResponseList.get(0).getPrice(), productResponseList.get(0).getCount() + 10);
        List<BuyProductResponse> productResponse = basketService.addProductToBasket(clientCookie, request);
        assertEquals(1, productResponse.size());
        assertEquals(productResponseList.get(0).getId(), productResponse.get(0).getId());
        assertEquals(productResponseList.get(0).getName(), productResponse.get(0).getName());
        assertEquals(productResponseList.get(0).getPrice(), productResponse.get(0).getPrice());
        assertEquals(10, productResponse.get(0).getCount() - productResponseList.get(0).getCount());

        request = new BuyProductRequest(productResponseList.get(1).getId(), productResponseList.get(1).getName(),
                productResponseList.get(1).getPrice(), null);
        List<BuyProductResponse> productResponse1 = basketService.addProductToBasket(clientCookie, request);
        assertEquals(2, productResponse1.size());
        assertEquals(productResponseList.get(1).getId(), productResponse1.get(1).getId());
        assertEquals(productResponseList.get(1).getName(), productResponse1.get(1).getName());
        assertEquals(productResponseList.get(1).getPrice(), productResponse1.get(1).getPrice());
        assertEquals(1, (long) productResponse1.get(1).getCount());

        request = new BuyProductRequest(productResponseList.get(1).getId(), productResponseList.get(1).getName(),
                60, productResponseList.get(1).getCount() - 1);
        List<BuyProductResponse> productResponse2 = basketService.addProductToBasket(clientCookie, request);
    }

    @Test(expected = OnlineShopExceptionOld.class)
    public void addProductToBasketTestError_Name() {
        List<AddProductResponse> productResponseList = addProduct();
        BuyProductRequest request;
        request = new BuyProductRequest(productResponseList.get(0).getId(), "errorName",
                productResponseList.get(0).getPrice(), productResponseList.get(0).getCount() + 10);
        List<BuyProductResponse> productResponse = basketService.addProductToBasket(clientCookie, request);
    }

    @Test(expected = OnlineShopExceptionOld.class)
    public void addProductToBasketTestError_Id() {
        List<AddProductResponse> productResponseList = addProduct();
        BuyProductRequest request;
        request = new BuyProductRequest(10, productResponseList.get(0).getName(),
                productResponseList.get(0).getPrice(), productResponseList.get(0).getCount() + 10);
        List<BuyProductResponse> productResponse = basketService.addProductToBasket(clientCookie, request);
    }

    @Test
    public void deleteProductFromBasketTest() {
        List<AddProductResponse> productResponseList = addProduct();
        BuyProductRequest request;
        List<BuyProductResponse> productResponse = new ArrayList<>();
        for (int i = 0; i < productResponseList.size(); i++) {
            request = new BuyProductRequest(productResponseList.get(i).getId(), productResponseList.get(i).getName(),
                    productResponseList.get(i).getPrice(), productResponseList.get(i).getCount() - i);
            productResponse = basketService.addProductToBasket(clientCookie, request);
        }
        for (int i = 0; i < productResponseList.size(); i++) {
            basketService.deleteProductFromBasketById(clientCookie, productResponse.get(i).getId());
            List<BuyProductResponse> productResponse2 = basketService.getBasket(clientCookie);
            assertEquals(productResponseList.size() - i - 1, productResponse2.size());
        }
    }

    @Test
    public void editProductAmountInBasketTest() {
        List<AddProductResponse> productResponseList = addProduct();
        BuyProductRequest request;
        List<BuyProductResponse> productResponse;
        for (int i = 0; i < productResponseList.size(); i++) {
            request = new BuyProductRequest(productResponseList.get(i).getId(), productResponseList.get(i).getName(),
                    productResponseList.get(i).getPrice(), productResponseList.get(i).getCount());
            productResponse = basketService.addProductToBasket(clientCookie, request);
        }
        request = new BuyProductRequest(productResponseList.get(0).getId(), productResponseList.get(0).getName(),
                productResponseList.get(0).getPrice(), 7);
        productResponse = basketService.editProductAmountInBasket(clientCookie, request);
        assertEquals(productResponseList.size(), productResponse.size());
        assertEquals(productResponseList.get(0).getId(), productResponse.get(0).getId());
        assertEquals(productResponseList.get(0).getName(), productResponse.get(0).getName());
        assertEquals(productResponseList.get(0).getPrice(), productResponse.get(0).getPrice());
        assertEquals(7, (long) productResponse.get(0).getCount());

        request = new BuyProductRequest(productResponseList.get(1).getId(), productResponseList.get(1).getName(),
                productResponseList.get(1).getPrice(), 100);
        productResponse = basketService.editProductAmountInBasket(clientCookie, request);
        assertEquals(productResponseList.size(), productResponse.size());
        assertEquals(productResponseList.get(1).getId(), productResponse.get(1).getId());
        assertEquals(productResponseList.get(1).getName(), productResponse.get(1).getName());
        assertEquals(productResponseList.get(1).getPrice(), productResponse.get(1).getPrice());
        assertEquals(100, (long) productResponse.get(1).getCount());
    }

    @Test(expected = OnlineShopExceptionOld.class)
    public void editProductAmountInBasketTestError_Name() {
        List<AddProductResponse> productResponseList = addProduct();
        BuyProductRequest request;
        List<BuyProductResponse> productResponse;
        for (int i = 0; i < productResponseList.size(); i++) {
            request = new BuyProductRequest(productResponseList.get(i).getId(), productResponseList.get(i).getName(),
                    productResponseList.get(i).getPrice(), productResponseList.get(i).getCount());
            productResponse = basketService.addProductToBasket(clientCookie, request);
        }
        request = new BuyProductRequest(productResponseList.get(0).getId(), "ErrorName",
                productResponseList.get(0).getPrice(), 7);
        productResponse = basketService.editProductAmountInBasket(clientCookie, request);
    }

    @Test(expected = OnlineShopExceptionOld.class)
    public void editProductAmountInBasketTestError_Price() {
        List<AddProductResponse> productResponseList = addProduct();
        BuyProductRequest request;
        List<BuyProductResponse> productResponse;
        for (int i = 0; i < productResponseList.size(); i++) {
            request = new BuyProductRequest(productResponseList.get(i).getId(), productResponseList.get(i).getName(),
                    productResponseList.get(i).getPrice(), productResponseList.get(i).getCount());
            productResponse = basketService.addProductToBasket(clientCookie, request);
        }
        request = new BuyProductRequest(productResponseList.get(0).getId(), productResponseList.get(0).getName(),
                101, 7);
        productResponse = basketService.editProductAmountInBasket(clientCookie, request);
    }

    @Test
    public void getBasketTest() {
        List<AddProductResponse> productResponseList = addProduct();
        BuyProductRequest request;
        List<BuyProductResponse> productResponse;
        for (int i = 0; i < productResponseList.size(); i++) {
            request = new BuyProductRequest(productResponseList.get(i).getId(), productResponseList.get(i).getName(),
                    productResponseList.get(i).getPrice(), productResponseList.get(i).getCount() - i);
            productResponse = basketService.addProductToBasket(clientCookie, request);
        }
        productResponse = basketService.getBasket(clientCookie);
        assertEquals(productResponseList.size(), productResponse.size());
    }
}

