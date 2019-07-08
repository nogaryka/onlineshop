package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
}
