package net.thumbtack.onlineshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "clients")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String postalAddress;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private Integer cash = 0;

//    @OneToMany(mappedBy = "")
//    private List<Basket> baskets;

    public Client(String firstName, String lastName, String patronymic, String login, String password, String email,
                  String phoneNumber, String postalAddress) {
        super(firstName, lastName, patronymic, login, password);
        this.email = email;
        this.postalAddress = postalAddress;
        this.phoneNumber = phoneNumber;
    }
}
