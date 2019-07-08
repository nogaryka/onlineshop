package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Basket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends CrudRepository<Basket, Integer> {
}
