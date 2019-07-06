package net.thumbtack.onlineshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Data
@Table(name = "baskets")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Basket {

    @Id
    private int id_client;
    @Id
    private int id_product;

   /* @EmbeddedId
    private ClientProduct clientProduct;

    @Embeddable
    @NoArgsConstructor
    class ClientProduct {
        private int id_client;
        private int id_product;
    }*/

}
