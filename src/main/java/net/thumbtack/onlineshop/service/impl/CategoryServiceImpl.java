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
import java.util.List;

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
            if (category.getIdParentCategory() == null) {
                category.setIdParentCategory(0);
            }
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
            if (categoryRepository.existsById(id)) {
                Category category = categoryRepository.findById(id).get();
                return new AddCategoryResponse(category.getId(), category.getName(), category.getIdParentCategory(),
                        getParentName(category.getIdParentCategory()));
            }
        } else {
            throw new OnlineShopExceptionOld("Данное действие доступно только для администратора");
        }
        return null;
    }

    @Override
    public AddCategoryResponse editCategory(String cookie, EditCategoryRequest request, Integer id) throws OnlineShopExceptionOld {
        SessionServiceImpl sessionService = getSessionService(administratorRepository, clientRepository, sessionRepository);
        if (request.getIdParent() == null && request.getName() == null) {
            throw new OnlineShopExceptionOld("Оба поля не могут быть пустыми");
        }
        if (sessionService.isAdmin(sessionService.getLogin(cookie))) {
            Category category = categoryRepository.findById(id).get();
            if ((category.getIdParentCategory() == 0 || category.getIdParentCategory() == null) && (request.getIdParent() != 0)) {
                throw new OnlineShopExceptionOld("Категория не может стать подкатегорией");
            } else if (category.getIdParentCategory() != 0 && (request.getIdParent() == 0 || request.getIdParent() == null)) {
                throw new OnlineShopExceptionOld("Подкатегория не может стать категорией");
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
        } else {
            throw new OnlineShopExceptionOld("Данное действие доступно только для администратора");
        }
    }

    @Override
    public void deleteCategoryById(String cookie, Integer id) throws OnlineShopExceptionOld {
        SessionServiceImpl sessionService = getSessionService(administratorRepository, clientRepository, sessionRepository);
        if (sessionService.isAdmin(sessionService.getLogin(cookie))) {
            if (categoryRepository.existsById(id)) {
                categoryRepository.deleteById(id);
            } else {
                throw new OnlineShopExceptionOld("Такой категории не существует");
            }
        } else {
            throw new OnlineShopExceptionOld("Данное действие доступно только для администратора");
        }
    }

    @Override
    public List<AddCategoryResponse> getCategoryList(String cookie) throws OnlineShopExceptionOld {
        Iterable<Category> categories = categoryRepository.findAllByIdParentCategoryEqualsOrderByNameAsc(0);
        Iterable<Category> subCategories = categoryRepository.findAllByIdParentCategoryGreaterThanOrderByNameAsc(0);
        List<AddCategoryResponse> responseList = new ArrayList<>();
        for (Category category : categories) {
            responseList.add(new AddCategoryResponse(category.getId(), category.getName(),
                    category.getIdParentCategory()));
            for (Category subCategory : subCategories) {
                if (subCategory.getIdParentCategory().equals(category.getId())) {
                    responseList.add(new AddCategoryResponse(subCategory.getId(), subCategory.getName(),
                            subCategory.getIdParentCategory()));
                }
            }
        }
        return responseList;
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
}