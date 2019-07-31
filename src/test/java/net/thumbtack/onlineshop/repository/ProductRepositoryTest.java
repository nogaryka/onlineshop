package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Category;
import net.thumbtack.onlineshop.entity.Product;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DebugService debugService;

    @After
    public void clearDB() {
        debugService.clearDB();
    }

    @Test
    public void insertProduct() {
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            productList.add(new Product("prod_" + i, 15, 1000 * i));
        }
        for (Product product : productList) {
            product = productRepository.save(product);
            assertTrue(product.getId() != 0);
        }
        List<Product> allProduct = IterableUtils.toList(productRepository.findAll());
        assertNotNull(allProduct);
        assertEquals(9, allProduct.size());
    }

    @Test
    public void insertProductWithCategory() {
        Product product = new Product("prodWithCategory", 1000, 10);
        product = productRepository.save(product);
        assertTrue(product.getId() != 0);
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            categories.add(new Category("category_" + i, 0));
        }
        for (Category category : categories) {
           category = categoryRepository.save(category);
           assertTrue(category.getId() != 0);
        }
        product.setCategories(categories);
        productRepository.save(product);
        Product productById =  productRepository.findById(product.getId()).get();
        assertEquals("prodWithCategory", productById.getName());
    }

    @Test
    public void findProductById() {
        Product product = new Product("prodWithCategory", 1000, 10);
        productRepository.save(product);
        assertTrue(product.getId() != 0);
        List<Category> categories = new ArrayList<>();
        List<Category> subCategories = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            categories.add(new Category("category_" + i, 0));
            subCategories.add(new Category("subCategory_" + i, 0));
        }
        for (int i = 0; i < 4; i++) {
            categoryRepository.save(categories.get(i));
            assertTrue(categories.get(i).getId() != 0);
            subCategories.get(i).setIdParentCategory(categories.get(i).getId());
            categoryRepository.save(subCategories.get(i));
            assertTrue(subCategories.get(i).getId() != 0);
        }
        product.setCategories(categories);
        product = productRepository.save(product);
        Product productById = productRepository.findById(product.getId()).get();
        assertEquals("prodWithCategory", productById.getName());
    }

    @Test
    public void updateProduct() {
        Product product = new Product("prodUpdate", 10, 0);
        productRepository.save(product);
        assertTrue(product.getId() != 0);
        product = productRepository.findById(product.getId()).get();
        product.setCount(1564);
        product.setPrice(6545);
        productRepository.save(product);
        Product productById = productRepository.findById(product.getId()).get();
        assertEquals(product.getName(), productById.getName());
        assertEquals(1564, (long) productById.getCount());
        assertEquals(6545, (long) productById.getPrice());
    }

    @Test
    public void deleteProductById() {
        Product product = new Product("prodDelete", 10, 0);
        productRepository.save(product);
        assertTrue(product.getId() != 0);
        productRepository.deleteById(product.getId());
        assertFalse(productRepository.existsById(product.getId()));
    }

    @Test
    public void findProductByCategorySortedByProductName() {
        List<String> name = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            name.add(i + "_Prod");
        }
        List<Category> categories;
        List<Category> catId = new ArrayList<>();
        for (int i = 0; i < name.size(); i++) {
            String s = name.get(i);
            categories = insertSetProduct(s);
            catId.add(categories.get(0)) ;
        }
        List<Product> productByCategoryWithEmptyMas = IterableUtils.toList(productRepository.findAllByCategoriesIsNullOrderByNameAsc());
        assertEquals(10, productByCategoryWithEmptyMas.size());
        assertTrue(productByCategoryWithEmptyMas.get(0).getCategories().isEmpty());
    }

    @Test
    public void findProductByCategorySortedByCategory() {
        List<String> name = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            name.add(i + "_Prod");
        }
        List<Category> categories;
        List<Category> catId = new ArrayList<>();
        for (int i = 0; i < name.size(); i++) {
            String s = name.get(i);
            categories = insertSetProduct(s);
            catId.add(categories.get(0));
        }
        List<Product> productByCategoryWithoutMas = IterableUtils.toList(productRepository.findAllByCategoriesIsNullOrderByNameAsc());
        assertTrue(productByCategoryWithoutMas.get(0).getCategories().isEmpty());
    }

    private List<Category> insertSetProduct(String prodName) {
        Product product = new Product(prodName, 1000, 10);
        productRepository.save(product);
        productRepository.save(new Product(prodName + "_WithoutCategory", 1000, 10));
        assertTrue(product.getId() != 0);
        List<Category> categories = new ArrayList<>();
        List<Category> subCategories = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            categories.add(new Category(prodName + "_cat_" + i, 0));
            subCategories.add(new Category(prodName + "_subCat_" + i, 0));
        }
        for (int i = 0; i < 4; i++) {
            categoryRepository.save(categories.get(i));
            assertTrue(categories.get(i).getId() != 0);
            subCategories.get(i).setIdParentCategory(categories.get(i).getId());
            categoryRepository.save(subCategories.get(i));
            assertTrue(subCategories.get(i).getId() != 0);
        }
        product.setCategories(categories);
        productRepository.save(product);
        product.setCategories(subCategories);
        productRepository.save(product);
        return categories;
    }
}
