package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdministratorServiceTest {

    @Autowired
    private AdministratorService administratorService;

    @Before
    public void before() {
    }

    @Test
    public void adminRegistration() {
        RegistrationAdminRequest request = new RegistrationAdminRequest("firstName", "lastName",
                "Patronymic", "login", "password", "Post");
        RegistrationAdminResponse response = administratorService.addAdmin(request);
        assertTrue(response.getId() != 0 || response.getId() != null);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getLogin(), response.getLogin());
        assertEquals(request.getPassword(), response.getPassword());
        assertEquals(request.getPost(), response.getPost());
    }

    @Test
    public void adminRegistrationNonPatronymic() {
        RegistrationAdminRequest request = new RegistrationAdminRequest("firstName", "lastName",
                null, "login", "password", "Post");
        RegistrationAdminResponse response = administratorService.addAdmin(request);
        assertTrue(response.getId() != 0 || response.getId() != null);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getLogin(), response.getLogin());
        assertEquals(request.getPassword(), response.getPassword());
        assertEquals(request.getPost(), response.getPost());
    }

    @Test
    public void adminRegistrationEmptyPatronymic() {
        RegistrationAdminRequest request = new RegistrationAdminRequest("firstName", "lastName",
                "", "login", "password", "Post");
        RegistrationAdminResponse response = administratorService.addAdmin(request);
        assertTrue(response.getId() != 0 || response.getId() != null);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getLogin(), response.getLogin());
        assertEquals(request.getPassword(), response.getPassword());
        assertEquals(request.getPost(), response.getPost());
    }

    /*@Test(expected = )
    public void adminRegistrationSomeBlankFields() {
        RegistrationAdminRequest request = new RegistrationAdminRequest("", null,
                "", "login", "password", "Post");
        RegistrationAdminResponse response = administratorService.addAdmin(request);
        assertTrue(response.getId() != 0 || response.getId() != null);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getLogin(), response.getLogin());
        assertEquals(request.getPassword(), response.getPassword());
        assertEquals(request.getPost(), response.getPost());
    }*/
}
