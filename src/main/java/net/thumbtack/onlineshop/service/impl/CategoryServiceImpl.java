package net.thumbtack.onlineshop.service.impl;

import net.thumbtack.onlineshop.dto.request.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.request.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.responce.AddCategoryResponse;
import net.thumbtack.onlineshop.entity.Category;
import net.thumbtack.onlineshop.exceptions.OnlineShopExceptionOld;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public AddCategoryResponse addCategory(String cookie, AddCategoryRequest request) throws OnlineShopExceptionOld {
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
    public AddCategoryResponse getCategoryById(String cookie, Integer id) throws OnlineShopExceptionOld {
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
    public AddCategoryResponse editCategory(String cookie, EditCategoryRequest request, Integer id) throws OnlineShopExceptionOld {
        SessionServiceImpl sessionService = getSessionService(administratorRepository, clientRepository, sessionRepository);
        if(request.getIdParent() == null && request.getName() == null) {
            throw new OnlineShopExceptionOld();
        }
        if (sessionService.isAdmin(sessionService.getLogin(cookie))) {
            Category category = categoryRepository.findById(id).get();
            if ((category.getIdParentCategory() == 0 || category.getIdParentCategory() == null) && (request.getIdParent() != 0)) {
                throw new OnlineShopExceptionOld();
            } else {
                if (StringUtils.isEmpty(request.getName())) {
                    category.setIdParentCategory(request.getIdParent());
                    categoryRepository.save(category);
                } else if (request.getIdParent() == null || request.getIdParent() == 0) {
                    category.setName(request.getName());
                    categoryRepository.save(category);
                } else {
                    category.setName(request.getName());
                    category.setIdParentCategory(request.getIdParent());
                    categoryRepository.save(category);
                }
                category = categoryRepository.findById(id).get();
                return new AddCategoryResponse(category.getId(), category.getName(), category.getIdParentCategory(),
                        getParentName(category.getIdParentCategory()));
            }
        }
        return null;
    }

    @Override
    public void deleteCategoryById(String cookie, Integer id) throws OnlineShopExceptionOld {
        SessionServiceImpl sessionService = getSessionService(administratorRepository, clientRepository, sessionRepository);
        if (sessionService.isAdmin(sessionService.getLogin(cookie))) {
            categoryRepository.deleteById(id);
        } else {
            throw new OnlineShopExceptionOld();
        }
    }

    @Override
    public List<AddCategoryResponse> getCategoryList(String cookie) throws OnlineShopExceptionOld {
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
            throw new OnlineShopExceptionOld();
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
            if (a.getIdParentCategory() == 0 || a.getIdParentCategory() == null) {
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