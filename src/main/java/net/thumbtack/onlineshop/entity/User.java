package net.thumbtack.onlineshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String patronymic;

    @Column(nullable = false, unique = true, updatable = false)
    private String login;

    @Column(nullable = false)
    private String password;
}
