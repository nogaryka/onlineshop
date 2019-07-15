package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Administrator;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends CrudRepository<Administrator, Integer> {
    @Query(value = "SELECT a.* FROM administrators a WHERE UPPER(a.login) = :login AND a.password = :password", nativeQuery = true)
    Optional<Administrator> findByLoginAndPassword(@Param("login") String login, @Param("password") String password);

    boolean existsByLogin(String login);

    Optional<Administrator> findByLogin(String login);

    @Transactional
    @Modifying
    @Query(value = "UPDATE administrators a SET a.first_name = :firstName, a.last_name = :lastName," +
            "a.patronymic = :patronymic, a.post = :post, a.password = :password WHERE a.id = :id", nativeQuery = true)
    void editAdmin(@Param("id") Integer id, @Param("firstName") String firstName, @Param("lastName") String lastName,
                   @Param("patronymic") String patronymic, @Param("password") String password,
                   @Param("post") String post);
}
