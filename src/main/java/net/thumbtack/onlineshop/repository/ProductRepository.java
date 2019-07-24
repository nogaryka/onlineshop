package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Category;
import net.thumbtack.onlineshop.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {//CrudRepository<Product, Integer>  {
   // Iterable<Product> findAllByCategory(Iterable<Integer> idCategory);

    Iterable<Product> findDistinctProductsByCategoriesIn(Iterable<Category> idCategories);
}
