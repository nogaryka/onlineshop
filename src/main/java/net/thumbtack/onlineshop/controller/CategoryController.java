package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.dto.request.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.request.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.responce.AddCategoryResponse;
import net.thumbtack.onlineshop.entity.Category;
import net.thumbtack.onlineshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static net.thumbtack.onlineshop.OnlineShopServer.COOKIE;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCategory(@CookieValue(COOKIE) String cookie, @Valid @RequestBody AddCategoryRequest request) {
        AddCategoryResponse response = service.addCategory(cookie, request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/api/categories/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCategoryById(@CookieValue(COOKIE) String cookie, @PathVariable("id") Integer id) {
        AddCategoryResponse response = service.getCategoryById(cookie, id);
        return ResponseEntity.ok().body(response);

    }

    @PutMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> editCategory(@CookieValue(COOKIE) String cookie,
                                          @Valid @RequestBody EditCategoryRequest request,
                                          @PathVariable("id") Integer id) {
        AddCategoryResponse response = service.editCategory(cookie, request, id);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteCategory(@CookieValue(COOKIE) String cookie, @PathVariable("id") Integer id) {
        service.deleteCategoryById(cookie, id);
        return ResponseEntity.ok("");
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllCategories(@CookieValue(COOKIE) String cookie,
                                              @RequestParam(value = "category", required = false) Integer[] idCategory,
                                              @RequestParam(value = "order", required = false, defaultValue = "product") String order) {
        List<AddCategoryResponse> list = service.getCategoryList(cookie);
        return ResponseEntity.ok("");
    }
}



