package net.thumbtack.onlineshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "administrators")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Administrator extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String post;

    public Administrator(String firstName, String lastName, String patronymic, String login, String password, String post) {
        super(firstName, lastName, patronymic, login, password);
        this.post = post;
    }
}
