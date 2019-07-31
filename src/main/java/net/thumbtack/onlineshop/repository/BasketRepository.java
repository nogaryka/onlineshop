package net.thumbtack.onlineshop.repository;

import net.thumbtack.onlineshop.entity.Basket;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BasketRepository extends PagingAndSortingRepository<Basket, Integer> { // CrudRepository<Basket, Integer> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM baskets  WHERE id_product_id = :idProduct AND id_client_id = :idClient", nativeQuery = true)
    void deleteByIdClientAndIdProduct(@Param("idClient") Integer idClient, @Param("idProduct") Integer idProduct);

    @Query(value = "UPDATE baskets b SET b.amount = :amount WHERE b.id_product_id = :idProduct AND b.id_client_id = :idClient", nativeQuery = true)
    @Modifying
    @Transactional
    void updateByIdClientAndIdProduct(@Param("idClient") Integer idClient, @Param("idProduct") Integer idProduct,
                                      @Param("amount") Integer amount);

    @Query(value = "SELECT b.* FROM baskets b WHERE b.id_client_id = :idClient", nativeQuery = true)
    @Transactional
    Iterable<Basket> findAllByIdClient(@Param("idClient") Integer idClient);
}
