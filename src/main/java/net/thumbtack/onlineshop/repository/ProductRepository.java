package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
   // Iterable<Product> findAllByCategory(Iterable<Integer> idCategory);
}
