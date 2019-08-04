package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.EditAccountAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationClientRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationClientResponse;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdminServiceTest {
    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private DebugService debugService;

    @Before
    public void before() {
        debugService.clearDB();
    }

    @Test
    public void adminRegistrationWithoutPatronymic() {
        RegistrationAdminRequest request = new RegistrationAdminRequest("Администратор", "Фамилия", null, "AdminLogin", "password", "admin");
        RegistrationAdminResponse response = administratorService.addAdmin(request);
        assertTrue(response.getId() != 0);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getPost(), response.getPost());
    }

    @Test
    public void adminRegistrationWithPatronymic() {
        RegistrationAdminRequest request = new RegistrationAdminRequest("Администратор", "Фамилия",
                "Администраторский", "AdminLogin", "password", "admin");
        RegistrationAdminResponse response = administratorService.addAdmin(request);
        assertTrue(response.getId() != 0);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getPost(), response.getPost());
    }

    @Test
    public void adminRegistrationEmptyPatronymic() {
        RegistrationAdminRequest request = new RegistrationAdminRequest("Администратор", "Фамилия",
                "", "AdminLogin", "password", "admin");
        RegistrationAdminResponse response = administratorService.addAdmin(request);
        assertTrue(response.getId() != 0);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getPost(), response.getPost());
    }

    @Test(expected = OnlineShopExceptionOld.class)
    public void adminRegistrationLogin_Error() {
        RegistrationClientRequest request = new RegistrationClientRequest("Клиент",
                "Фамилия", "Отчество", "AdminLogin", "password123",
                "client@gmail.com", "Client_Address", "79131533464");
        RegistrationAdminRequest request2 = new RegistrationAdminRequest("Администратор", "Фамилия",
                "Администраторский", "AdminLogin", "password", "admin");
        RegistrationClientResponse response = clientService.addClient(request);
        RegistrationAdminResponse response2 = administratorService.addAdmin(request2);
    }

    @Test
    public void adminEditFields() {
        RegistrationAdminRequest registrationAdminRequest = new RegistrationAdminRequest("Администратор",
                "Фамилия", "Администраторский", "AdminLogin", "password",
                "admin");
        RegistrationAdminResponse adminResponse = administratorService.addAdmin(registrationAdminRequest);
        EditAccountAdminRequest editAccountAdminRequest = new EditAccountAdminRequest("Админ",
                "ФамилияРед", "", "password", "password123",
                "super admin");
        RegistrationAdminResponse adminResponse2 = administratorService.editProfileAdmin(adminResponse.getToken(),
                editAccountAdminRequest);
        assertTrue(adminResponse2.getId() != 0);
        assertEquals(editAccountAdminRequest.getFirstName(), adminResponse2.getFirstName());
        assertEquals(editAccountAdminRequest.getPatronymic(), adminResponse2.getPatronymic());
        assertEquals(editAccountAdminRequest.getPost(), adminResponse2.getPost());
    }

    @Test(expected = OnlineShopExceptionOld.class)
    public void adminEditFieldsError_NullCookie() {
        RegistrationAdminRequest registrationAdminRequest = new RegistrationAdminRequest("Администратор",
                "Фамилия", "Администраторский", "AdminLogin", "password",
                "admin");
        RegistrationAdminResponse adminResponse = administratorService.addAdmin(registrationAdminRequest);
        EditAccountAdminRequest editAccountAdminRequest = new EditAccountAdminRequest("Админ",
                "ФамилияРед", "", "password", "password123",
                "super admin");
        RegistrationAdminResponse adminResponse2 = administratorService.editProfileAdmin(null,
                editAccountAdminRequest);
    }

    @Test(expected = OnlineShopExceptionOld.class)
    public void adminEditFieldsError_EmptyCookie() {
        RegistrationAdminRequest registrationAdminRequest = new RegistrationAdminRequest("Администратор",
                "Фамилия", "Администраторский", "AdminLogin", "password",
                "admin");
        RegistrationAdminResponse adminResponse = administratorService.addAdmin(registrationAdminRequest);
        EditAccountAdminRequest editAccountAdminRequest = new EditAccountAdminRequest("Админ",
                "ФамилияРед", "", "password", "password123",
                "super admin");
        RegistrationAdminResponse adminResponse2 = administratorService.editProfileAdmin("",
                editAccountAdminRequest);
    }

    @Test(expected = OnlineShopExceptionOld.class)
    public void adminEditFieldsError_NotRegistration() {
        RegistrationAdminRequest registrationAdminRequest = new RegistrationAdminRequest("Администратор",
                "Фамилия", "Администраторский", "AdminLogin", "password",
                "admin");
        RegistrationAdminResponse adminResponse = administratorService.addAdmin(registrationAdminRequest);
        EditAccountAdminRequest editAccountAdminRequest = new EditAccountAdminRequest("Админ",
                "ФамилияРед", "", "password", "password123",
                "super admin");
        debugService.clearDB();
        RegistrationAdminResponse adminResponse2 = administratorService.editProfileAdmin("",
                editAccountAdminRequest);
    }
}
