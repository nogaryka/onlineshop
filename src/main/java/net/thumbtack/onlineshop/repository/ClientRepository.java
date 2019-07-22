package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
    @Query(value = "SELECT c.* FROM clients c WHERE UPPER(c.login) = :login AND c.password = :password", nativeQuery = true)
    Optional<Client> findByLoginAndPassword(@Param("login") String login, @Param("password") String password);

    boolean existsByLogin(String login);

    Optional<Client> findByLogin(String login);

    @Transactional
    @Modifying
    @Query(value = "UPDATE clients c SET c.first_name = :firstName, c.last_name = :lastName," +
            "c.patronymic = :patronymic, c.password = :password, c.email = :email, " +
            "c.postal_address = :postalAddress, c.phone_number = :phoneNumber WHERE c.id = :id", nativeQuery = true)
    void editClient(@Param("id") Integer id, @Param("firstName") String firstName, @Param("lastName") String lastName,
                   @Param("patronymic") String patronymic, @Param("password") String password,
                   @Param("email") String email, @Param("postalAddress") String postalAddress,
                   @Param("phoneNumber") String phoneNumber);

}
