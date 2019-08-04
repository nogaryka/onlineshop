package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.request.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.request.RegistrationAdminRequest;
import net.thumbtack.onlineshop.dto.responce.AddCategoryResponse;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
public class CategoryTest {

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
    public void addCategory() throws Exception {
        EditCategoryRequest request = new EditCategoryRequest("Category", 0);
        MvcResult addCategoryResult = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Category\"}"))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse categoryResponse = objectMapper.readValue(addCategoryResult.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(categoryResponse.getNameParent());
        assertEquals(0, (long) categoryResponse.getIdParentCategory());
        assertEquals("Category", categoryResponse.getName());
    }

    @Test
    public void addCategoryAndSubcategory() throws Exception {
        AddCategoryRequest rootCategory = new AddCategoryRequest("Category", 0);
        MvcResult addCategoryResult = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse = objectMapper.readValue(addCategoryResult.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse.getNameParent());
        assertEquals(0, (long) rootCategoryResponse.getIdParentCategory());
        AddCategoryRequest childCategory = new AddCategoryRequest("Subcategory", rootCategoryResponse.getId());
        MvcResult addChildCategoryResult = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(childCategory)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse childCategoryResponse = objectMapper.readValue(addChildCategoryResult.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertEquals(rootCategoryResponse.getName(), childCategoryResponse.getNameParent());
        assertEquals(rootCategoryResponse.getId(), childCategoryResponse.getIdParentCategory());
        assertEquals(childCategory.getName(), childCategoryResponse.getName());
    }


    @Test
    public void getCategory() throws Exception {
        AddCategoryRequest rootCategory = new AddCategoryRequest("Category", 0);
        MvcResult addCategoryResult = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse = objectMapper.readValue(addCategoryResult.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse.getNameParent());
        assertEquals(0, (long)rootCategoryResponse.getIdParentCategory());
        MvcResult getCategoryResult = mockMvc.perform(get("/api/categories/{id}", String.valueOf(rootCategoryResponse.getId()))
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse getCategoryResponse = objectMapper.readValue(getCategoryResult.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertEquals(rootCategoryResponse.getName(), getCategoryResponse.getName());
        assertEquals(rootCategoryResponse.getId(), getCategoryResponse.getId());
    }

    @Test
    public void putCategory() throws Exception {
        AddCategoryRequest rootCategory = new AddCategoryRequest("Root_Category", 0);
        MvcResult addCategoryResult = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse = objectMapper.readValue(addCategoryResult.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse.getNameParent());
        assertEquals(0, (long) rootCategoryResponse.getIdParentCategory());
        EditCategoryRequest putCategory = new EditCategoryRequest("Category_Root", 0);
        MvcResult getCategoryResult = mockMvc.perform(put("/api/categories/{id}", String.valueOf(rootCategoryResponse.getId()))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putCategory)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse putCategoryResponse = objectMapper.readValue(getCategoryResult.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertEquals(rootCategoryResponse.getId(), putCategoryResponse.getId());
        assertNotEquals(rootCategoryResponse.getName(), putCategoryResponse.getName());
    }

    @Test
    public void putCategoryToSubcategory_withError() throws Exception {
        AddCategoryRequest rootCategory1 = new AddCategoryRequest("Root_Category-1", 0);
        MvcResult addCategoryResult1 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory1)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse1 = objectMapper.readValue(addCategoryResult1.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse1.getNameParent());
        assertEquals(0, (long)rootCategoryResponse1.getIdParentCategory());
        AddCategoryRequest rootCategory2 = new AddCategoryRequest("Root_Category-2", 0);
        MvcResult addCategoryResult2 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory2)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse2 = objectMapper.readValue(addCategoryResult2.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse2.getNameParent());
        assertEquals(0, (long) rootCategoryResponse2.getIdParentCategory());
        EditCategoryRequest putCategory = new EditCategoryRequest(rootCategoryResponse1.getName(), rootCategoryResponse2.getId());
        mockMvc.perform(put("/api/categories/{id}", String.valueOf(rootCategoryResponse1.getId()))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putCategory)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void putSubcategory() throws Exception {
        AddCategoryRequest rootCategory1 = new AddCategoryRequest("Root_Category-1", 0);
        MvcResult addCategoryResult1 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory1)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse1 = objectMapper.readValue(addCategoryResult1.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse1.getNameParent());
        assertEquals(0, (long)rootCategoryResponse1.getIdParentCategory());
        AddCategoryRequest rootCategory2 = new AddCategoryRequest("Root_Category-2", 0);
        MvcResult addCategoryResult2 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory2)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse2 = objectMapper.readValue(addCategoryResult2.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse2.getNameParent());
        assertEquals(0, (long) rootCategoryResponse2.getIdParentCategory());
        AddCategoryRequest childCategory = new AddCategoryRequest("Child_Category", rootCategoryResponse1.getId());
        MvcResult addChildCategoryResult = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(childCategory)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse childCategoryResponse = objectMapper.readValue(addChildCategoryResult.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertEquals(rootCategoryResponse1.getName(), childCategoryResponse.getNameParent());
        assertEquals(rootCategoryResponse1.getId(), childCategoryResponse.getIdParentCategory());
        EditCategoryRequest putCategory = new EditCategoryRequest(null, rootCategoryResponse2.getId());
        MvcResult getCategoryResult = mockMvc.perform(put("/api/categories/{category_number}", String.valueOf(childCategoryResponse.getId()))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putCategory)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse putCategoryResponse = objectMapper.readValue(getCategoryResult.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertEquals(rootCategoryResponse2.getId(), putCategoryResponse.getIdParentCategory());
        assertEquals(rootCategoryResponse2.getName(), putCategoryResponse.getNameParent());
    }

    @Test
    public void putSubcategoryToCategory_withError() throws Exception {
        AddCategoryRequest rootCategory = new AddCategoryRequest("Root_Category", 0);
        MvcResult addCategoryResult1 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse = objectMapper.readValue(addCategoryResult1.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse.getNameParent());
        assertEquals(0, (long) rootCategoryResponse.getIdParentCategory());
        AddCategoryRequest childCategory = new AddCategoryRequest("Child_Category", rootCategoryResponse.getId());
        MvcResult addChildCategoryResult = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(childCategory)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse childCategoryResponse = objectMapper.readValue(addChildCategoryResult.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertEquals(rootCategoryResponse.getName(), childCategoryResponse.getNameParent());
        assertEquals(rootCategoryResponse.getId(), childCategoryResponse.getIdParentCategory());
        EditCategoryRequest putCategory = new EditCategoryRequest("Child_Category", 0);
        mockMvc.perform(put("/api/categories/{category_number}", String.valueOf(childCategoryResponse.getId()))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putCategory)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void putCategoryWithEmptyRequest_withError() throws Exception {
        AddCategoryRequest rootCategory = new AddCategoryRequest("Root_Category", 0);
        MvcResult addCategoryResult1 = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse = objectMapper.readValue(addCategoryResult1.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse.getNameParent());
        assertEquals(0, (long) rootCategoryResponse.getIdParentCategory());
        AddCategoryRequest childCategory = new AddCategoryRequest("Child_Category", rootCategoryResponse.getId());
        MvcResult addChildCategoryResult = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(childCategory)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse childCategoryResponse = objectMapper.readValue(addChildCategoryResult.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertEquals(rootCategoryResponse.getName(), childCategoryResponse.getNameParent());
        assertEquals(rootCategoryResponse.getId(), childCategoryResponse.getIdParentCategory());
        EditCategoryRequest putCategory = new EditCategoryRequest(null, 0);
        MvcResult getCategoryResult = mockMvc.perform(put("/api/categories/{id}", String.valueOf(childCategoryResponse.getId()))
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putCategory)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deleteCategory() throws Exception {
        AddCategoryRequest rootCategory = new AddCategoryRequest("Root_Category", 0);
        MvcResult addCategoryResult = mockMvc.perform(post("/api/categories")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rootCategory)))
                .andExpect(status().isOk())
                .andReturn();
        AddCategoryResponse rootCategoryResponse = objectMapper.readValue(addCategoryResult.getResponse().getContentAsString(), AddCategoryResponse.class);
        assertNull(rootCategoryResponse.getNameParent());
        assertEquals(0, (long) rootCategoryResponse.getIdParentCategory());
        MvcResult result = mockMvc.perform(delete("/api/categories/{id}", String.valueOf(rootCategoryResponse.getId()))
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("", result.getResponse().getContentAsString());
    }

    @Test
    public void getAllCategories() throws Exception {
        List<AddCategoryRequest> requestList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            requestList.add(new AddCategoryRequest("Root_Category_" + i, 0));
        }
        for (AddCategoryRequest request : requestList) {
            MvcResult addCategoryResult = mockMvc.perform(post("/api/categories")
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andReturn();
        }
        MvcResult getResult = mockMvc.perform(get("/api/categories")
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
        List<AddCategoryResponse> categoryResponseList = objectMapper.readValue(getResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, AddCategoryResponse.class));
        assertFalse(categoryResponseList.isEmpty());
    }
}


