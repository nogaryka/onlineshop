package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {
    @Query(value = "SELECT COUNT(*) > 0 FROM purchases " +
            "WHERE id_product_id = :idProduct AND id_client_id = :idClient", nativeQuery = true)
    Integer existsByClientAndProduct(@Param("idClient") Integer idClient, @Param("idProduct") Integer idProduct);

    @Query(value = "SELECT p.* FROM purchases p" +
            " WHERE p.id_product_id = :idProduct AND p.id_client_id = :idClient", nativeQuery = true)
    Optional<Purchase> findByClientAndProduct(@Param("idClient") Integer idClient, @Param("idProduct") Integer idProduct);

    @Query(value = "SELECT * FROM purchases p LEFT JOIN products_categories pc ON p.id_product_id = pc.id_product " +
            "WHERE pc.id_product IS NULL LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Purchase> findAllByCategoryIsNull(@Param("offset") Integer offset, @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM purchases p LEFT JOIN products_categories pc ON p.id_product_id = pc.id_product " +
            "WHERE pc.id_category IN :idCategory " +
            "GROUP BY p.id_product_id, p.id_client_id LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Purchase> findAllByCategory(@Param("idCategory") List<Integer> idCategory, @Param("offset") Integer offset,
                                         @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM purchases p " +
            "WHERE p.id_product_id IN :idProduct LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Purchase> findAllByIdProduct(@Param("idProduct") List<Integer> idProduct, @Param("offset") Integer offset,
                                          @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM purchases p " +
            "WHERE p.id_client_id IN :idClient LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Purchase> findAllByIdClient(@Param("idClient") List<Integer> Client, @Param("offset") Integer offset,
                                         @Param("limit") Integer limit);


    @Query(value = "SELECT * FROM purchases p WHERE p.id_client_id IN :idClient AND  p.id_product_id IN " +
            "(SELECT p.id_product_id FROM purchases p LEFT JOIN products_categories pc ON p.id_product_id = pc.id_product " +
            "WHERE pc.id_product IS NULL) LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Purchase> findAllByIdClientAndCategoryIsNull(@Param("idClient") List<Integer> idClient,
                                                          @Param("offset") Integer offset,
                                                          @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM purchases p WHERE p.id_client_id IN :idClient AND  p.id_product_id IN " +
            "(SELECT p.id_product_id FROM purchases p LEFT JOIN products_categories pc ON p.id_product_id = pc.id_product " +
            "WHERE pc.id_category IN :idCategory GROUP BY p.id_product_id, p.id_client_id) " +
            "LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Purchase> findAllByIdClientAndCategory(@Param("idCategory") List<Integer> idCategory,
                                                    @Param("idClient") List<Integer> idClient,
                                                    @Param("offset") Integer offset,
                                                    @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM purchases p " +
            "WHERE p.id_client_id IN :idClient AND p.id_product_id IN :idProduct " +
            "LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Purchase> findAllByIdProductAndClient(@Param("idProduct") List<Integer> idProduct,
                                                   @Param("idClient") List<Integer> idClient,
                                                   @Param("offset") Integer offset,
                                                   @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM purchases LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Purchase> findAll(@Param("offset") Integer offset, @Param("limit") Integer limit);
}
