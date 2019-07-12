package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Administrator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends CrudRepository<Administrator, Integer> {
    @Query("SELECT a FROM administrators a WHERE UPPER(a.login) in :login AND a.password in :password")
    Optional<Administrator> findByLogin(@Param("login") String login, @Param("password") String password);
}
