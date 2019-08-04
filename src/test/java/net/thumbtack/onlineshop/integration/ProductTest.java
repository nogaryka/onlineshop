package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.request.EditProductRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.responce.AddCategoryResponse;
import net.thumbtack.onlineshop.dto.responce.AddProductResponse;
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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
public class ProductTest {

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
        RegistrationAdminRequest request = new RegistrationAdminRequest("Администратор", "Фамилия", "Отчество", "Adminlogin", "password", "admin");
        MvcResult registrationResult = mockMvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(COOKIE))
                .andReturn();
        cookie = registrationResult.getResponse().getCookie(COOKIE);
        assertNotNull(cookie);
    }

    @Test
    public void addProductWithoutCategory() throws Exception {
        AddProductRequest request = new AddProductRequest("Product", 100, 10, null);
        MvcResult addProductResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse simpleProductResponse = objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertTrue(simpleProductResponse.getId() != 0);
        assertNull(simpleProductResponse.getIdCategories());

    }

    @Test
    public void addProductWithCategory() throws Exception {
        EditCategoryRequest rootCategory1 = new EditCategoryRequest("Root_Category-1", 0);
        MvcResult addCategoryResult1 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory1)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse1 = objectMapper.readValue(addCategoryResult1.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse1.getNameParent());
        assertEquals(0, (long) rootCategoryResponse1.getIdParentCategory());
        EditCategoryRequest rootCategory2 = new EditCategoryRequest("Root_Category-2", 0);
        MvcResult addCategoryResult2 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory2)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse2 = objectMapper.readValue(addCategoryResult2.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse2.getNameParent());
        assertEquals(0, (long) rootCategoryResponse2.getIdParentCategory());
        List<Integer> categories = new ArrayList<>();
        categories.add(rootCategoryResponse1.getId());
        categories.add(rootCategoryResponse2.getId());
        AddProductRequest request = new AddProductRequest("Product", 100, 10, categories);
        MvcResult addProductResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse simpleProductResponse = objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertTrue(simpleProductResponse.getId() != 0);
        assertEquals(2, simpleProductResponse.getIdCategories().size());
    }

    @Test
    public void addProductWithoutCount() throws Exception {
        AddProductRequest request = new AddProductRequest("Product", 100);
        MvcResult addProductResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse simpleProductResponse = objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertTrue(simpleProductResponse.getId() != 0);
        assertNull(simpleProductResponse.getIdCategories());
    }

    @Test
    public void addProductWith_0_Prise() throws Exception {
        AddProductRequest request = new AddProductRequest("Product", 0);
        mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void putProduct_All() throws Exception {
        EditCategoryRequest rootCategory1 = new EditCategoryRequest("Root_Category-1", 0);
        MvcResult addCategoryResult1 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory1)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse1 = objectMapper.readValue(addCategoryResult1.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse1.getNameParent());
        assertEquals(0, (long) rootCategoryResponse1.getIdParentCategory());
        EditCategoryRequest rootCategory2 = new EditCategoryRequest("Root_Category-2", 0);
        MvcResult addCategoryResult2 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory2)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse2 = objectMapper.readValue(addCategoryResult2.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse2.getNameParent());
        assertEquals(0, (long) rootCategoryResponse2.getIdParentCategory());
        List<Integer> categories = new ArrayList<>();
        categories.add(rootCategoryResponse1.getId());
        categories.add(rootCategoryResponse2.getId());
        AddProductRequest request = new AddProductRequest("Product", 100, 10, categories);
        MvcResult addProductResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse simpleProductResponse = objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertTrue(simpleProductResponse.getId() != 0);
        assertEquals(10, (long) simpleProductResponse.getCount());
        assertNotNull(simpleProductResponse.getIdCategories());
        assertEquals(2, simpleProductResponse.getIdCategories().size());
        List<Integer> categories2 = new ArrayList<>();
        categories2.add(rootCategoryResponse1.getId());
        EditProductRequest putRequest = new EditProductRequest("NewNameProduct", 99, 9, categories2);
        MvcResult putProductResult = mockMvc.perform(put("/api/products/{id}", String.valueOf(simpleProductResponse.getId()))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putRequest)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse putProductResponse = objectMapper.readValue(putProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertEquals(simpleProductResponse.getId(), putProductResponse.getId());
        assertNotEquals(simpleProductResponse.getPrice(), putProductResponse.getPrice());
        assertNotEquals(simpleProductResponse.getCount(), putProductResponse.getCount());
        assertNotEquals(simpleProductResponse.getName(), putProductResponse.getName());
        assertNotEquals(simpleProductResponse.getIdCategories().size(), putProductResponse.getIdCategories().size());
    }

    @Test
    public void putProduct_WithoutCategory() throws Exception {
        EditCategoryRequest rootCategory1 = new EditCategoryRequest("Root_Category-1", 0);
        MvcResult addCategoryResult1 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory1)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse1 = objectMapper.readValue(addCategoryResult1.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse1.getNameParent());
        assertEquals(0, (long) rootCategoryResponse1.getIdParentCategory());
        EditCategoryRequest rootCategory2 = new EditCategoryRequest("Root_Category-2", 0);
        MvcResult addCategoryResult2 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory2)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse2 = objectMapper.readValue(addCategoryResult2.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse2.getNameParent());
        assertEquals(0, (long) rootCategoryResponse2.getIdParentCategory());
        List<Integer> categories = new ArrayList<>();
        categories.add(rootCategoryResponse1.getId());
        categories.add(rootCategoryResponse2.getId());
        AddProductRequest request = new AddProductRequest("Product", 100, 10, categories);
        MvcResult addProductResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse simpleProductResponse = objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertTrue(simpleProductResponse.getId() != 0);
        assertEquals(10, (long) simpleProductResponse.getCount());
        assertNotNull(simpleProductResponse.getIdCategories());
        assertEquals(2, simpleProductResponse.getIdCategories().size());
        EditProductRequest putRequest = new EditProductRequest(null, null, null, new ArrayList<>());
        MvcResult putProductResult = mockMvc.perform(put("/api/products/{product_number}", String.valueOf(simpleProductResponse.getId()))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putRequest)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse putProductResponse = objectMapper.readValue(putProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertEquals(simpleProductResponse.getId(), putProductResponse.getId());
        assertNotEquals(simpleProductResponse.getIdCategories(), putProductResponse.getIdCategories());
        assertTrue(putProductResponse.getIdCategories().isEmpty());
    }

    @Test
    public void putProduct_WithName() throws Exception {
        EditCategoryRequest rootCategory1 = new EditCategoryRequest("Root_Category-1", 0);
        MvcResult addCategoryResult1 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory1)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse1 = objectMapper.readValue(addCategoryResult1.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse1.getNameParent());
        assertEquals(0, (long) rootCategoryResponse1.getIdParentCategory());
        EditCategoryRequest rootCategory2 = new EditCategoryRequest("Root_Category-2", 0);
        MvcResult addCategoryResult2 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory2)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse2 = objectMapper.readValue(addCategoryResult2.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse2.getNameParent());
        assertEquals(0, (long) rootCategoryResponse2.getIdParentCategory());
        List<Integer> categories = new ArrayList<>();
        categories.add(rootCategoryResponse1.getId());
        categories.add(rootCategoryResponse2.getId());
        AddProductRequest request = new AddProductRequest("Product", 100, 10, categories);
        MvcResult addProductResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse simpleProductResponse = objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertTrue(simpleProductResponse.getId() != 0);
        assertEquals(10, (long) simpleProductResponse.getCount());
        assertNotNull(simpleProductResponse.getIdCategories());
        assertEquals(2, simpleProductResponse.getIdCategories().size());
        EditProductRequest putRequest = new EditProductRequest("NewNameProduct", null, null, null);
        MvcResult putProductResult = mockMvc.perform(put("/api/products/{product_number}", String.valueOf(simpleProductResponse.getId()))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putRequest)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse putProductResponse = objectMapper.readValue(putProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertEquals(simpleProductResponse.getId(), putProductResponse.getId());
        assertEquals(simpleProductResponse.getPrice(), putProductResponse.getPrice());
        assertEquals(simpleProductResponse.getCount(), putProductResponse.getCount());
        assertEquals(simpleProductResponse.getIdCategories().size(), putProductResponse.getIdCategories().size());
        assertNotEquals(simpleProductResponse.getName(), putProductResponse.getName());
    }

    @Test
    public void putProduct_WithPrice() throws Exception {
        AddProductRequest request = new AddProductRequest("Product", 100, 10, null);
        MvcResult addProductResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse simpleProductResponse = objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertTrue(simpleProductResponse.getId() != 0);
        assertEquals(10, (long) simpleProductResponse.getCount());
        assertNull(simpleProductResponse.getIdCategories());
        EditProductRequest putRequest = new EditProductRequest(null, 99, null, null);
        MvcResult putProductResult = mockMvc.perform(put("/api/products/{product_number}", String.valueOf(simpleProductResponse.getId()))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putRequest)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse putProductResponse = objectMapper.readValue(putProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertEquals(simpleProductResponse.getId(), putProductResponse.getId());
        assertNotEquals(simpleProductResponse.getPrice(), putProductResponse.getPrice());
        assertEquals(simpleProductResponse.getCount(), putProductResponse.getCount());
        assertEquals(simpleProductResponse.getName(), putProductResponse.getName());
    }

    @Test
    public void putProduct_WithCount() throws Exception {
        AddProductRequest request = new AddProductRequest("Product", 100, 10, null);
        MvcResult addProductResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse simpleProductResponse = objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertTrue(simpleProductResponse.getId() != 0);
        assertEquals(10, (long) simpleProductResponse.getCount());
        assertNull(simpleProductResponse.getIdCategories());
        EditProductRequest putRequest = new EditProductRequest(null, null, 1234, null);
        MvcResult putProductResult = mockMvc.perform(put("/api/products/{product_number}", String.valueOf(simpleProductResponse.getId()))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putRequest)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse putProductResponse = objectMapper.readValue(putProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertEquals(simpleProductResponse.getId(), putProductResponse.getId());
        assertEquals(simpleProductResponse.getPrice(), putProductResponse.getPrice());
        assertNotEquals(simpleProductResponse.getCount(), putProductResponse.getCount());
        assertEquals(simpleProductResponse.getName(), putProductResponse.getName());
    }

    @Test
    public void deleteProductWithoutCategory() throws Exception {
        AddProductRequest request = new AddProductRequest("Product", 100, 10, null);
        MvcResult addProductResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse simpleProductResponse = objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertTrue(simpleProductResponse.getId() != 0);
        assertNull(simpleProductResponse.getIdCategories());
        MvcResult deleteProductResult = mockMvc.perform(delete("/api/products/{product_number}", String.valueOf(simpleProductResponse.getId()))
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("", deleteProductResult.getResponse().getContentAsString());
    }

    @Test
    public void deleteProductWithCategory() throws Exception {
        EditCategoryRequest rootCategory1 = new EditCategoryRequest("Root_Category-1", 0);
        MvcResult addCategoryResult1 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory1)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse1 = objectMapper.readValue(addCategoryResult1.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse1.getNameParent());
        assertEquals(0, (long) rootCategoryResponse1.getIdParentCategory());
        EditCategoryRequest rootCategory2 = new EditCategoryRequest("Root_Category-2", 0);
        MvcResult addCategoryResult2 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory2)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse2 = objectMapper.readValue(addCategoryResult2.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse2.getNameParent());
        assertEquals(0, (long) rootCategoryResponse2.getIdParentCategory());
        List<Integer> categories = new ArrayList<>();
        categories.add(rootCategoryResponse1.getId());
        categories.add(rootCategoryResponse2.getId());
        AddProductRequest request = new AddProductRequest("Product", 100, 10, categories);
        MvcResult addProductResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse simpleProductResponse = objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertTrue(simpleProductResponse.getId() != 0);
        assertEquals(10, (long) simpleProductResponse.getCount());
        assertNotNull(simpleProductResponse.getIdCategories());
        assertEquals(2, simpleProductResponse.getIdCategories().size());
        MvcResult deleteProductResult = mockMvc.perform(delete("/api/products/{product_number}", simpleProductResponse.getId())
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("", deleteProductResult.getResponse().getContentAsString());
    }

    @Test
    public void getProductWithoutCategory() throws Exception {
        AddProductRequest request = new AddProductRequest("Product", 100, 10, null);
        MvcResult addProductResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse simpleProductResponse = objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertTrue(simpleProductResponse.getId() != 0);
        assertNull(simpleProductResponse.getIdCategories());
        MvcResult getProductResult = mockMvc.perform(get("/api/products/{product_number}", simpleProductResponse.getId())
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse productWithCategoryNameResponse = objectMapper.readValue(getProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertEquals(simpleProductResponse.getId(), productWithCategoryNameResponse.getId());
        assertTrue(productWithCategoryNameResponse.getIdCategories().isEmpty());
    }

    @Test
    public void getProductWithCategory() throws Exception {
        EditCategoryRequest rootCategory1 = new EditCategoryRequest("Root_Category-1", 0);
        MvcResult addCategoryResult1 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory1)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse1 = objectMapper.readValue(addCategoryResult1.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse1.getNameParent());
        assertEquals(0, (long) rootCategoryResponse1.getIdParentCategory());
        EditCategoryRequest rootCategory2 = new EditCategoryRequest("Root_Category-2", 0);
        MvcResult addCategoryResult2 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory2)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse2 = objectMapper.readValue(addCategoryResult2.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse2.getNameParent());
        assertEquals(0, (long) rootCategoryResponse2.getIdParentCategory());
        List<Integer> categories = new ArrayList<>();
        categories.add(rootCategoryResponse1.getId());
        categories.add(rootCategoryResponse2.getId());
        AddProductRequest request = new AddProductRequest("Product", 100, 10, categories);
        MvcResult addProductResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse simpleProductResponse = objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertTrue(simpleProductResponse.getId() != 0);
        assertNotNull(simpleProductResponse.getIdCategories());
        MvcResult getProductResult = mockMvc.perform(get("/api/products/{product_number}", simpleProductResponse.getId())
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        AddProductResponse productWithCategoryNameResponse = objectMapper.readValue(getProductResult.getResponse().getContentAsString(), AddProductResponse.class);
        assertEquals(simpleProductResponse.getId(), productWithCategoryNameResponse.getId());
        assertEquals(2, productWithCategoryNameResponse.getIdCategories().size());
    }

    private List<AddCategoryResponse> insertCategory() throws Exception {
        List<AddCategoryResponse> categoryResponses = new ArrayList<>();
        for (int i = 6; i > 0; i--) {
            MvcResult addCategoryResult1 = mockMvc.perform(post("/api/categories")
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new EditCategoryRequest(i + "_Category", 0))))
                    .andExpect(status().isOk())
                    .andReturn();
            AddCategoryResponse rootCategoryResponse = objectMapper.readValue(addCategoryResult1.getResponse().getContentAsString(), AddCategoryResponse.class);
            categoryResponses.add(rootCategoryResponse);
            assertTrue(rootCategoryResponse.getId() != 0);
        }
        return categoryResponses;
    }

    private List<AddProductResponse> insertProduct(List<AddCategoryResponse> categoryResponses) throws Exception {
        List<AddProductResponse> productResponses = new ArrayList<>();
        AddProductRequest request = new AddProductRequest("ProductWithoutCategory", 100, 10, null);
        MvcResult mvcResult = mockMvc.perform(post("/api/products")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        productResponses.add(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AddProductResponse.class));
        for (int i = 0; i < 5; i++) {
            List<Integer> category = new ArrayList<>();
            category.add(categoryResponses.get(i).getId());
            category.add(categoryResponses.get(i + 1).getId());
            AddProductRequest requestProd = new AddProductRequest(i + "_ProductWithCategory", 100, 100, category);
            MvcResult addProductResult = mockMvc.perform(post("/api/products")
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestProd)))
                    .andExpect(status().isOk())
                    .andReturn();
            productResponses.add(objectMapper.readValue(addProductResult.getResponse().getContentAsString(), AddProductResponse.class));
        }
        return productResponses;
    }


    @Test
    public void getProductWithParam_orderProduct() throws Exception {
        List<AddCategoryResponse> categoryResponses = insertCategory();
        List<AddProductResponse> productResponses = insertProduct(categoryResponses);
        MvcResult getResult = mockMvc.perform(get("/api/products")
                .cookie(cookie)
                .param("order", "product"))
                .andExpect(status().isOk())
                .andReturn();
        List<AddProductResponse> responseList = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, AddProductResponse.class));
        assertEquals(6, responseList.size());
    }

    @Test
    public void getProductWithParam_orderProduct_withCategory() throws Exception {
        List<AddCategoryResponse> categoryResponses = insertCategory();
        List<AddProductResponse> productResponses = insertProduct(categoryResponses);
        MvcResult getResult = mockMvc.perform(get("/api/products")
                .cookie(cookie)
                .param("category", String.valueOf(categoryResponses.get(0).getId()), String.valueOf(categoryResponses.get(2).getId()))
                .param("order", "product"))
                .andExpect(status().isOk())
                .andReturn();
        List<AddProductResponse> responseList = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, AddProductResponse.class));
        assertEquals(3, responseList.size());
    }

    @Test
    public void getProductWithParam_orderCategory() throws Exception {
        List<AddCategoryResponse> categoryResponses = insertCategory();
        List<AddProductResponse> productResponses = insertProduct(categoryResponses);
        MvcResult getResult = mockMvc.perform(get("/api/products")
                .cookie(cookie)
                .param("order", "category"))
                .andExpect(status().isOk())
                .andReturn();
        List<AddProductResponse> responseList = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, AddProductResponse.class));
        assertEquals(11, responseList.size());
    }

    @Test
    public void getProductWithParam_orderCategory_withCategory() throws Exception {
        List<AddCategoryResponse> categoryResponses = insertCategory();
        List<AddProductResponse> productResponses = insertProduct(categoryResponses);
        MvcResult getResult = mockMvc.perform(get("/api/products")
                .cookie(cookie)
                .param("category", String.valueOf(categoryResponses.get(0).getId()), String.valueOf(categoryResponses.get(2).getId()))
                .param("order", "category"))
                .andExpect(status().isOk())
                .andReturn();
        List<AddProductResponse> responseList = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, AddProductResponse.class));
        assertEquals(4, responseList.size());
    }

    @Test
    public void getProductWithParam_orderDefault() throws Exception {
        List<AddCategoryResponse> categoryResponses = insertCategory();
        List<AddProductResponse> productResponses = insertProduct(categoryResponses);
        MvcResult getResult = mockMvc.perform(get("/api/products")
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        List<AddProductResponse> responseList = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, AddProductResponse.class));
        assertEquals(6, responseList.size());
    }

    @Test
    public void getProductWithParam_orderDefault_withCategory() throws Exception {
        List<AddCategoryResponse> categoryResponses = insertCategory();
        List<AddProductResponse> productResponses = insertProduct(categoryResponses);
        MvcResult getResult = mockMvc.perform(get("/api/products")
                .cookie(cookie)
                .param("category", String.valueOf(categoryResponses.get(0).getId()), String.valueOf(categoryResponses.get(2).getId())))
                .andExpect(status().isOk())
                .andReturn();
        List<AddProductResponse> responseList = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, AddProductResponse.class));
        assertEquals(3, responseList.size());
    }
}
