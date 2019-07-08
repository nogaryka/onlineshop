package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addCategory(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/api/categories{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getCategoryById(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");

    }

    @PutMapping(value = "/api/categories{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> editCategory(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @DeleteMapping(value = "/api/categories{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteCategory(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllCategories(@RequestBody String name, HttpServletRequest response) {
        return ResponseEntity.ok("");
    }
}



