package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Category;
import net.thumbtack.onlineshop.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {//CrudRepository<Product, Integer>  {
    Iterable<Product> findDistinctProductsByCategoriesInOrderByNameAsc(Iterable<Category> idCategories);

    Iterable<Product> findAllByCategoriesInOrderByCategoriesNameAsc(Iterable<Category> idCategories);

    Iterable<Product> findAllByCategoriesIsNullOrderByNameAsc();
}
