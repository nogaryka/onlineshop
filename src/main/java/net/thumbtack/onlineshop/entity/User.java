package net.thumbtack.onlineshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String patronymic;

    @NotNull
    @Column(unique = true)
    private String login;

    @NotNull
    private String password;
}
