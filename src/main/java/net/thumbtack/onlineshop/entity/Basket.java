package net.thumbtack.onlineshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "baskets")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Basket {
    @EmbeddedId
    private IdClientAndProduct idClientAndProduct;

    private Integer amount;


    @Data
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IdClientAndProduct  implements Serializable{
        @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        @JoinColumn
        private Client idClient;

        @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        @JoinColumn
        private Product idProduct;
    }
}
