package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
}
