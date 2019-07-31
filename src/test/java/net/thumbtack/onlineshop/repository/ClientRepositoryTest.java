package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Client;
import net.thumbtack.onlineshop.service.DebugService;
import org.apache.commons.collections4.IterableUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DebugService debugService;

    @After
    public void clearDB() {
        debugService.clearDB();
    }

    @Test
    public void testInsertAndFindAll() {
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            clients.add(new Client("user" + i, "last_name" + i, null, "LOGIN" + i, "password", "box_" + i + "@yandex.com", "7913153346" + i, "address" + i));
        }
        for (Client client : clients) {
            client.setCash(1111);
            clientRepository.save(client);
        }
        List<Client> allClient = IterableUtils.toList(clientRepository.findAll());
        for (int i = 0; i < 10; i++) {
            assertEquals(clients.get(i).getLogin(), allClient.get(i).getLogin());
            assertEquals("LOGIN" + i, allClient.get(i).getLogin());
            assertEquals(1111, (long) allClient.get(i).getCash());
        }
    }

    @Test
    public void testFindClientById() {
        Client client = new Client("userTest", "last_nameTest", null, "loginTest", "passwordTest", "box_Test@yandex.com", "11111111111", "address");
        client.setCash(1000);
        clientRepository.save(client);
        Client clientFromDB =  clientRepository.findById(client.getId()).get();
        assertEquals(1000, (long) clientFromDB.getCash());
    }


    @Test
    public void testUpdateClient() {
        Client client = new Client("userTest", "last_nameTest", null, "loginTest", "passwordTest", "box_Test@yandex.com", "11111111111", "address");
        client = clientRepository.save(client);
        assertTrue(client.getId() != 0);
        client.setPhoneNumber("");
        client.setEmail("");
        client.setPostalAddress("");
        clientRepository.save(client);
        Client clientUpdate =  clientRepository.findById(client.getId()).get();
        assertTrue(clientUpdate.getPhoneNumber().isEmpty());
        assertTrue(clientUpdate.getEmail().isEmpty());
        assertTrue(clientUpdate.getPostalAddress().isEmpty());
    }
}
