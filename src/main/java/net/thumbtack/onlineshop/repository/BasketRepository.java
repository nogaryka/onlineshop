package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Basket;
import net.thumbtack.onlineshop.entity.Purchase;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends PagingAndSortingRepository<Basket, Integer> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM baskets " +
            "WHERE id_product_id = :idProduct AND id_client_id = :idClient", nativeQuery = true)
    void deleteByIdClientAndIdProduct(@Param("idClient") Integer idClient, @Param("idProduct") Integer idProduct);

    @Query(value = "UPDATE baskets b SET b.amount = :amount " +
            "WHERE b.id_product_id = :idProduct AND b.id_client_id = :idClient", nativeQuery = true)
    @Modifying
    @Transactional
    void updateByIdClientAndIdProduct(@Param("idClient") Integer idClient, @Param("idProduct") Integer idProduct,
                                      @Param("amount") Integer amount);

    @Query(value = "SELECT b.* FROM baskets b WHERE b.id_client_id = :idClient", nativeQuery = true)
    @Transactional
    Iterable<Basket> findAllByIdClient(@Param("idClient") Integer idClient);

    @Query(value = "SELECT COUNT(*) > 0 FROM baskets " +
            "WHERE id_product_id = :idProduct AND id_client_id = :idClient", nativeQuery = true)
    Integer existsByClientAndProduct(@Param("idClient") Integer idClient, @Param("idProduct") Integer idProduct);

    @Query(value = "SELECT b.* FROM baskets b" +
            " WHERE b.id_product_id = :idProduct AND b.id_client_id = :idClient", nativeQuery = true)
    Optional<Basket> findByClientAndProduct(@Param("idClient") Integer idClient, @Param("idProduct") Integer idProduct);

    @Query(value = "SELECT * FROM baskets b LEFT JOIN products_categories pc ON b.id_product_id = pc.id_product " +
            "WHERE pc.id_product IS NULL LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Basket> findAllByCategoryIsNull(@Param("offset") Integer offset, @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM baskets b LEFT JOIN products_categories pc ON b.id_product_id = pc.id_product " +
            "WHERE pc.id_category IN :idCategory " +
            "GROUP BY b.id_product_id, b.id_client_id LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Basket> findAllByCategory(@Param("idCategory") List<Integer> idCategory, @Param("offset") Integer offset,
                                         @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM baskets b " +
            "WHERE b.id_product_id IN :idProduct LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Basket> findAllByIdProduct(@Param("idProduct") List<Integer> idProduct, @Param("offset") Integer offset,
                                          @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM baskets b " +
            "WHERE b.id_client_id IN :idClient LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Basket> findAllByIdClient(@Param("idClient") List<Integer> Client, @Param("offset") Integer offset,
                                         @Param("limit") Integer limit);


    @Query(value = "SELECT * FROM baskets b WHERE b.id_client_id IN :idClient AND  b.id_product_id IN " +
            "(SELECT b.id_product_id FROM baskets b LEFT JOIN products_categories pc ON b.id_product_id = pc.id_product " +
            "WHERE pc.id_product IS NULL) LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Basket> findAllByIdClientAndCategoryIsNull(@Param("idClient") List<Integer> idClient,
                                                          @Param("offset") Integer offset,
                                                          @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM baskets WHERE b.id_client_id IN :idClient AND  b.id_product_id IN " +
            "(SELECT b.id_product_id FROM baskets b LEFT JOIN products_categories pc ON b.id_product_id = pc.id_product " +
            "WHERE pc.id_category IN :idCategory GROUP BY b.id_product_id, b.id_client_id) " +
            "LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Basket> findAllByIdClientAndCategory(@Param("idCategory") List<Integer> idCategory,
                                                    @Param("idClient") List<Integer> idClient,
                                                    @Param("offset") Integer offset,
                                                    @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM baskets b " +
            "WHERE b.id_client_id IN :idClient AND b.id_product_id IN :idProduct " +
            "LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Basket> findAllByIdProductAndClient(@Param("idProduct") List<Integer> idProduct,
                                                   @Param("idClient") List<Integer> idClient,
                                                   @Param("offset") Integer offset,
                                                   @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM baskets LIMIT :offset, :limit", nativeQuery = true)
    Iterable<Basket> findAll(@Param("offset") Integer offset, @Param("limit") Integer limit);
}
