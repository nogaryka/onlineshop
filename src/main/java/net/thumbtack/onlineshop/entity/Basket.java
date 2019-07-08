package net.thumbtack.onlineshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "baskets")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Basket implements Serializable {
    @EmbeddedId
    private IdClientAndProduct idClientAndProduct;

    private Integer amount;

    @Embeddable
    private class IdClientAndProduct  implements Serializable{
        @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        @JoinColumn
        private Client idClient;

        @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        @JoinColumn
        private Product idProduct;
    }
}
