package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Administrator;
import net.thumbtack.onlineshop.service.DebugService;
import org.junit.After;
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
public class AdminRepositoryTest {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private DebugService debugService;

    @After
    public void clearDB() {
        debugService.clearDB();
    }

    @Test
    public void testInsertAndFindById() {
        Administrator admin = new Administrator("user", "last_name", null, "login", "password", "administrator");
        administratorRepository.save(admin);
        assertTrue(admin.getId() != 0);
        Administrator adminFromDB = administratorRepository.findById(admin.getId()).get();
        assertEquals(admin, adminFromDB);
    }

    @Test
    public void updateAdmin() {
        Administrator admin = new Administrator("user", "last_name", null, "login", "password", "administrator");
        administratorRepository.save(admin);
        assertTrue(admin.getId() != 0);
        admin.setFirstName("UserForever");
        administratorRepository.save(admin);
        Administrator updateAdmin = administratorRepository.findById(admin.getId()).get();
        assertEquals(admin, updateAdmin);
    }
}
