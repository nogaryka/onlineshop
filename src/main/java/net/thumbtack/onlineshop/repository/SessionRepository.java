package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends CrudRepository<Session, Integer> {
    void deleteByToken(String token);

    boolean existsByToken(String token);

    Optional<Session> findByToken(String token);
}
