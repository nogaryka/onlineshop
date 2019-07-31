package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Category;
import net.thumbtack.onlineshop.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {//CrudRepository<Product, Integer>  {
    Iterable<Product> findDistinctProductsByCategoriesInOrderByNameAsc(Iterable<Category> idCategories);

    Iterable<Product> findAllByCategoriesIsNullOrderByNameAsc();

    Iterable<Product> findAllByOrderByNameAsc();

    boolean existsByNameAndIdNot(String name, Integer id);
}
