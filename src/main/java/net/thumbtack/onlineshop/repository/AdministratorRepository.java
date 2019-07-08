package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Administrator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends CrudRepository<Administrator, Integer> {

}
