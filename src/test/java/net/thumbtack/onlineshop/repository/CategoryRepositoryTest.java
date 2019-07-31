package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Category;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DebugService debugService;

    @After
    public void clearDB() {
        debugService.clearDB();
    }

    @Test
    public void insertCategory() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            categories.add(new Category("category" + i, null));
        }
        for (Category category : categories) {
            category = categoryRepository.save(category);
            assertTrue(category.getId() != 0);
        }
        List<Category> subCategories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            subCategories.add(new Category("sub_category" + i, categories.get(0).getId()));
        }
        for (Category category : subCategories) {
            category = categoryRepository.save(category);
            assertTrue(category.getId() != 0);
        }
    }

    @Test
    public void findCategoryById() {
        Set<Category> categories = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            categories.add(new Category("category" + i, 0));
        }
        for (Category category : categories) {
            categoryRepository.save(category);
            Set<Category> subCategories = new HashSet<>();
            for (int i = 100; i < 115; i++) {
                subCategories.add(new Category(i + "subcategory" + category.getId(), category.getId()));
            }
            for (Category subCategory : subCategories) {
                categoryRepository.save(subCategory);
            }
        }
        List<Category> allCategory = IterableUtils.toList(categoryRepository.findAll());
        for (Category cat : allCategory) {
            assertNotEquals(20, (long) cat.getIdParentCategory());
        }
    }

    @Test
    public void findAllCategory() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Root_category", 0));
        categoryRepository.save(categories.get(0));
        for (int i = 1; i < 10; i++) {
            categories.add(new Category("category" + i, categories.get(i - 1).getId()));
            categoryRepository.save(categories.get(i));
        }
        for (Category category : categories) {
            assertTrue(category.getId() != 0);
        }
    }

    @Test
    public void updateCategory() {
        Category category1 = new Category("categoryTest1", 0);
        Category category2 = new Category("categoryTest2", null);
        category1 = categoryRepository.save(category1);
        category2 = categoryRepository.save(category2);
        assertTrue(category1.getId() != 0);
        assertTrue(category2.getId() != 0);
        Category subCategory = new Category("subCategoryTest", category1.getId());
        subCategory = categoryRepository.save(subCategory);
        assertTrue(subCategory.getId() != 0);
        subCategory.setName("SomeTestName");
        subCategory.setIdParentCategory(category2.getId());
        subCategory = categoryRepository.save(subCategory);
        assertEquals(subCategory.getIdParentCategory(), category2.getId());
    }

    @Test
    public void deleteCategoryById() {
        Category category = new Category("categoryTest1", 0);
        categoryRepository.save(category);
        categoryRepository.deleteById(category.getId());
        boolean categoryById = categoryRepository.existsById(category.getId());
        assertFalse(categoryById);
    }
}
