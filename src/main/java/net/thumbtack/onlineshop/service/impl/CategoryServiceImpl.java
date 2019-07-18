package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.dto.request.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.request.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.responce.AddCategoryResponse;
import net.thumbtack.onlineshop.entity.Category;
import net.thumbtack.onlineshop.entity.Session;
import net.thumbtack.onlineshop.exceptions.OnlineShopException;
import net.thumbtack.onlineshop.repository.AdministratorRepository;
import net.thumbtack.onlineshop.repository.CategoryRepository;
import net.thumbtack.onlineshop.repository.ClientRepository;
import net.thumbtack.onlineshop.repository.SessionRepository;
import net.thumbtack.onlineshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final AdministratorRepository administratorRepository;
    private final SessionRepository sessionRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, AdministratorRepository administratorRepository,
                               SessionRepository sessionRepository, ClientRepository clientRepository) {
        this.categoryRepository = categoryRepository;
        this.administratorRepository = administratorRepository;
        this.sessionRepository = sessionRepository;
        this.clientRepository = clientRepository;
    }


    @Override
    public AddCategoryResponse addCategory(String cookie, AddCategoryRequest request) throws OnlineShopException {
        SessionServiceImpl sessionService = getSessionService(administratorRepository, clientRepository, sessionRepository);
        if (sessionService.isAdmin(sessionService.getLogin(cookie))) {
            Category category = new Category(request.getName(), request.getIdParentCategory());
            category = categoryRepository.save(category);
            return new AddCategoryResponse(category.getId(), category.getName(), category.getIdParentCategory(),
                    getParentName(category.getIdParentCategory()));
        }
        return null;
    }

    @Override
    public AddCategoryResponse getCategoryById(String cookie, Integer id) throws OnlineShopException {
        SessionServiceImpl sessionService = getSessionService(administratorRepository, clientRepository, sessionRepository);
        if (sessionService.isAdmin(sessionService.getLogin(cookie))) {
            Category category = categoryRepository.findById(id).get();
            return new AddCategoryResponse(category.getId(), category.getName(), category.getIdParentCategory(),
                    getParentName(category.getIdParentCategory()));
        } else {
            return null;
        }
    }

    @Override
    public AddCategoryResponse editCategory(String cookie, EditCategoryRequest request, Integer id) throws OnlineShopException {
        SessionServiceImpl sessionService = getSessionService(administratorRepository, clientRepository, sessionRepository);
        if (sessionService.isAdmin(sessionService.getLogin(cookie))) {
            Category category = categoryRepository.findById(id).get();
            if ((category.getIdParentCategory() == 0 || category.getIdParentCategory() == null)
                    && (request.getIdParent() != 0 || request.getIdParent() != null)) {
                throw new OnlineShopException();
            } else {
                if (StringUtils.isEmpty(request.getName())) {
                    categoryRepository.editIdParent(id, request.getIdParent());
                } else if (request.getIdParent() == 0 || request.getIdParent() == null) {
                    categoryRepository.editName(id, request.getName());
                } else {
                    categoryRepository.editCategory(id, request.getName(), request.getIdParent());
                }
                Category category2 = categoryRepository.findById(id).get();
                return new AddCategoryResponse(category2.getId(), category2.getName(), category2.getIdParentCategory(),
                        getParentName(category2.getIdParentCategory()));
            }
        }
        return null;
    }

    @Override
    public void deleteCategoryById(String cookie, Integer id) throws OnlineShopException {
        SessionServiceImpl sessionService = getSessionService(administratorRepository, clientRepository, sessionRepository);
        if (sessionService.isAdmin(sessionService.getLogin(cookie))) {
            categoryRepository.deleteById(id);
        } else {
            throw new OnlineShopException();
        }
    }

    @Override
    public List<AddCategoryResponse> getCategoryList(String cookie) throws OnlineShopException {
        Map<Integer, AddCategoryResponse> response = new HashMap<>();
        SessionServiceImpl sessionService = getSessionService(administratorRepository, clientRepository, sessionRepository);
        if (sessionService.isAdmin(sessionService.getLogin(cookie))) {
            Iterable<Category> list = categoryRepository.findAll();
            for (Category category : list) {
                response.put(category.getId(), new AddCategoryResponse(category.getId(), category.getName(),
                        category.getIdParentCategory()));
            }
            response = setNameForSubCategories(response);
            return sortCategory(response);
        } else {
            throw new OnlineShopException();
        }
    }

    public String getParentName(Integer idParent) {
        if (idParent == null || idParent == 0) {
            return null;
        } else if (categoryRepository.existsById(idParent)) {
            return categoryRepository.findById(idParent).get().getName();
        } else {
            return null;
        }
    }

    public SessionServiceImpl getSessionService(AdministratorRepository administratorRepository,
                                                ClientRepository clientRepository, SessionRepository sessionRepository) {
        return new SessionServiceImpl(administratorRepository, clientRepository, sessionRepository);
    }

    public List<AddCategoryResponse> sortCategory(Map<Integer, AddCategoryResponse> categories) {
        List<AddCategoryResponse> list = new ArrayList<>();
        for (Map.Entry<Integer, AddCategoryResponse> category : categories.entrySet()) {
            list.add(category.getValue());
        }
        Collections.sort(list, (AddCategoryResponse a, AddCategoryResponse b) -> {
            if(a.getIdParentCategory() == 0 || a.getIdParentCategory() == null) {
                return a.getName().compareTo(b.getName());
            } else {
                return a.getNameParent().compareTo(b.getName());
            }
        });
        return null;
    }

    public Map<Integer, AddCategoryResponse> setNameForSubCategories(Map<Integer, AddCategoryResponse> categories) {
        for (Map.Entry<Integer, AddCategoryResponse> category : categories.entrySet()) {
            if (category.getValue().getIdParentCategory() != 0 || category.getValue().getIdParentCategory() == null) {
                category.getValue().setNameParent(categories.get(category.getValue().getIdParentCategory()).getName());
            }
        }
        return categories;
    }
}