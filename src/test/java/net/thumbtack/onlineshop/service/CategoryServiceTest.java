package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.request.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.request.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.responce.AddCategoryResponse;
import net.thumbtack.onlineshop.dto.responce.RegistrationAdminResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static net.thumbtack.onlineshop.config.ConstConfig.COOKIE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DebugService debugService;

    private String cookie;

    @After
    public void clearDB() {
        debugService.clearDB();
    }

    @Before
    public void before() {
        RegistrationAdminRequest request = new RegistrationAdminRequest("Ivan", "Maksimov",
                "Petrovich", "vsehGlava1", "123456", "Glava");
        ResponseEntity<RegistrationAdminResponse> response = restTemplate.postForEntity("/api/admins", request, RegistrationAdminResponse.class);
        cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        cookie = cookie.substring(cookie.indexOf('=') + 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(cookie);
        assertTrue(response.getBody().getId() != 0);
        assertEquals(request.getFirstName(), response.getBody().getFirstName());
        assertEquals(request.getPost(), response.getBody().getPost());
    }

    @Test
    public void add_root_and_child_category() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", COOKIE + "=" + cookie);
        HttpEntity requestEntity = new HttpEntity(new AddCategoryRequest("Root_Category", 0), requestHeaders);
        ResponseEntity<AddCategoryResponse> rootCategoryResponse = restTemplate.exchange("/api/categories", HttpMethod.POST, requestEntity, AddCategoryResponse.class);
        assertEquals(HttpStatus.OK, rootCategoryResponse.getStatusCode());
        assertEquals(0, (long) rootCategoryResponse.getBody().getIdParentCategory());

        HttpHeaders requestHeaders2 = new HttpHeaders();
        requestHeaders2.add("Cookie", COOKIE + "=" + cookie);
        HttpEntity requestEntity2 = new HttpEntity(new AddCategoryRequest("Child_Category", rootCategoryResponse.getBody().getId()), requestHeaders2);
        ResponseEntity<AddCategoryResponse> rootCategoryResponse2 = restTemplate.exchange("/api/categories", HttpMethod.POST, requestEntity2, AddCategoryResponse.class);
        assertEquals(HttpStatus.OK, rootCategoryResponse2.getStatusCode());
        assertEquals(rootCategoryResponse.getBody().getId(), rootCategoryResponse2.getBody().getIdParentCategory());
        assertEquals(rootCategoryResponse.getBody().getName(), rootCategoryResponse2.getBody().getNameParent());
    }


    @Test
    public void add_category_and_put_category_and_get_category() {
        HttpHeaders addRequestHeaders = new HttpHeaders();
        addRequestHeaders.add("Cookie", COOKIE + "=" + cookie);
        HttpEntity addRequestEntity = new HttpEntity(new AddCategoryRequest("Root_Category", 0), addRequestHeaders);
        ResponseEntity<AddCategoryResponse> addRootCategoryResponse = restTemplate.exchange("/api/categories", HttpMethod.POST, addRequestEntity, AddCategoryResponse.class);
        assertEquals(HttpStatus.OK, addRootCategoryResponse.getStatusCode());
        assertNull(addRootCategoryResponse.getBody().getNameParent());
        assertEquals(0, (long) addRootCategoryResponse.getBody().getIdParentCategory());

        HttpHeaders putRequestHeaders = new HttpHeaders();
        putRequestHeaders.add("Cookie", COOKIE + "=" + cookie);
        HttpEntity putRequestEntity = new HttpEntity(new EditCategoryRequest("RootRootCategory", 0), putRequestHeaders);
        ResponseEntity<AddCategoryResponse> putCategoryResponse = restTemplate.exchange("/api/categories/{category_number}", HttpMethod.PUT, putRequestEntity, AddCategoryResponse.class, addRootCategoryResponse.getBody().getId());
        assertEquals(HttpStatus.OK, putCategoryResponse.getStatusCode());
        assertEquals("RootRootCategory", putCategoryResponse.getBody().getName());

        HttpHeaders getRequestHeaders = new HttpHeaders();
        getRequestHeaders.add("Cookie", COOKIE + "=" + cookie);
        HttpEntity getRequestEntity = new HttpEntity(null, getRequestHeaders);
        ResponseEntity<AddCategoryResponse> getCategoryResponse = restTemplate.exchange("/api/categories/{category_number}", HttpMethod.GET, getRequestEntity, AddCategoryResponse.class, addRootCategoryResponse.getBody().getId());
        assertEquals(HttpStatus.OK, getCategoryResponse.getStatusCode());
        assertEquals(putCategoryResponse.getBody().getName(), getCategoryResponse.getBody().getName());
        assertEquals(putCategoryResponse.getBody().getId(), getCategoryResponse.getBody().getId());
    }

    @Test
    public void put_root_category_to_child_with_error() {
        HttpHeaders addRequestHeaders = new HttpHeaders();
        addRequestHeaders.add("Cookie", COOKIE + "=" + cookie);
        HttpEntity addRequestEntity = new HttpEntity(new AddCategoryRequest("Root_Category", 0), addRequestHeaders);
        ResponseEntity<AddCategoryResponse> addRootCategoryResponse = restTemplate.exchange("/api/categories", HttpMethod.POST, addRequestEntity, AddCategoryResponse.class);
        assertEquals(HttpStatus.OK, addRootCategoryResponse.getStatusCode());
        assertNull(addRootCategoryResponse.getBody().getNameParent());
        assertEquals(0, (long) addRootCategoryResponse.getBody().getIdParentCategory());

        HttpHeaders addRabbitHeaders = new HttpHeaders();
        addRabbitHeaders.add("Cookie", COOKIE + "=" + cookie);
        HttpEntity addRabbitEntity = new HttpEntity(new AddCategoryRequest("Rabbit_Category", 0), addRabbitHeaders);
        ResponseEntity<AddCategoryResponse> addRabbitResponse = restTemplate.exchange("/api/categories", HttpMethod.POST, addRabbitEntity, AddCategoryResponse.class);
        assertEquals(HttpStatus.OK, addRabbitResponse.getStatusCode());
        assertNull(addRabbitResponse.getBody().getNameParent());
        assertEquals(0, (long) addRabbitResponse.getBody().getIdParentCategory());

        HttpHeaders putRequestHeaders = new HttpHeaders();
        putRequestHeaders.add("Cookie", COOKIE + "=" + cookie);
        HttpEntity putRequestEntity = new HttpEntity(new EditCategoryRequest(null, addRootCategoryResponse.getBody().getId()), putRequestHeaders);
        ResponseEntity<AddCategoryResponse> putCategoryResponse = restTemplate.exchange("/api/categories/{category_number}", HttpMethod.PUT, putRequestEntity, AddCategoryResponse.class, addRabbitResponse.getBody().getId());
        assertEquals(HttpStatus.BAD_REQUEST, putCategoryResponse.getStatusCode());
    }

    @Test
    public void add_category_and_delete_category_and_get_category_with_error() {
        HttpHeaders addRequestHeaders = new HttpHeaders();
        addRequestHeaders.add("Cookie", COOKIE+ "=" + cookie);
        HttpEntity addRequestEntity = new HttpEntity(new AddCategoryRequest("Root_Category", 0), addRequestHeaders);
        ResponseEntity<AddCategoryResponse> addRootCategoryResponse = restTemplate.exchange("/api/categories", HttpMethod.POST, addRequestEntity, AddCategoryResponse.class);
        assertEquals(HttpStatus.OK, addRootCategoryResponse.getStatusCode());
        assertNull(addRootCategoryResponse.getBody().getNameParent());
        assertEquals(0, (long) addRootCategoryResponse.getBody().getIdParentCategory());

        HttpHeaders deleteRequestHeaders = new HttpHeaders();
        deleteRequestHeaders.add("Cookie", COOKIE+ "=" + cookie);
        HttpEntity deleteRequestEntity = new HttpEntity(null, deleteRequestHeaders);
        ResponseEntity<String> deleteCategoryResponse = restTemplate.exchange("/api/categories/{category_number}", HttpMethod.DELETE, deleteRequestEntity, String.class, addRootCategoryResponse.getBody().getId());
        assertEquals(HttpStatus.OK, deleteCategoryResponse.getStatusCode());
    }

    @Test
    public void getAllCategories() {
        for (int i = 0; i < 5; i++) {
            HttpHeaders addRequestHeaders = new HttpHeaders();
            addRequestHeaders.add("Cookie", COOKIE + "=" + cookie);
            HttpEntity addRequestEntity = new HttpEntity(new AddCategoryRequest("Root_Category_" + i, 0), addRequestHeaders);
            ResponseEntity<AddCategoryResponse> addRootCategoryResponse = restTemplate.exchange("/api/categories", HttpMethod.POST, addRequestEntity, AddCategoryResponse.class);
            assertEquals(HttpStatus.OK, addRootCategoryResponse.getStatusCode());
        }
        HttpHeaders getRequestHeaders = new HttpHeaders();
        getRequestHeaders.add("Cookie", COOKIE + "=" + cookie);
        HttpEntity getRequestEntity = new HttpEntity(null, getRequestHeaders);
        ResponseEntity<List<AddCategoryResponse>> getCategoryResponse = restTemplate.exchange("/api/categories", HttpMethod.GET, getRequestEntity, new ParameterizedTypeReference<List<AddCategoryResponse>>() {
        });
        assertEquals(HttpStatus.OK, getCategoryResponse.getStatusCode());
        assertEquals(5, getCategoryResponse.getBody().size());
    }
}
