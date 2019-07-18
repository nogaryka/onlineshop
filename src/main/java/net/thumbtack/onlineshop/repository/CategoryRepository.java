package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE categories c SET c.name = :name, c.id_parent-category = :idParentCategory WHERE c.id = :id",
            nativeQuery = true)
    void editCategory(@Param("id") Integer id, @Param("name") String name,
                      @Param("idParentCategory") Integer idParentCategory);

    @Transactional
    @Modifying
    @Query(value = "UPDATE categories c SET c.id_parent-category = :idParentCategory WHERE c.id = :id", nativeQuery = true)
    void editIdParent(@Param("id") Integer id, @Param("idParentCategory") Integer idParentCategory);

    @Transactional
    @Modifying
    @Query(value = "UPDATE categories c SET c.name = :name WHERE c.id = :id", nativeQuery = true)
    void editName(@Param("id") Integer id, @Param("name") String name);
}
