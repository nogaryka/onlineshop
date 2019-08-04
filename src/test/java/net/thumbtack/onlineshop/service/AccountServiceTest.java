package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationClientRequest;
import net.thumbtack.onlineshop.dto.responce.InformarionAdoutClientsForAdminResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationUserResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AccountServiceTest {
    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccaountService accaountService;

    @Autowired
    private DebugService debugService;

    @Before
    public void before() {
        debugService.clearDB();
    }

    @Test
    public void infoAboutUserTest() {
        RegistrationAdminRequest request = new RegistrationAdminRequest("Администратор", "Фамилия",
                "Администраторский", "AdminLogin", "password", "admin");
        RegistrationAdminResponse response = administratorService.addAdmin(request);
        RegistrationUserResponse infoAdmin = accaountService.getInfoAboutMe(response.getToken());
        assertTrue(response.equals(infoAdmin));
        RegistrationClientRequest request2 = new RegistrationClientRequest("Клиент",
                "Фамилия", "Отчество", "ClientLogin", "password123",
                "client@gmail.com", "Client_Address", "79131533464");
        RegistrationClientResponse response2 = clientService.addClient(request2);
        RegistrationUserResponse infoClient = accaountService.getInfoAboutMe(response2.getToken());
        assertTrue(response2.equals(infoClient));
    }
}
