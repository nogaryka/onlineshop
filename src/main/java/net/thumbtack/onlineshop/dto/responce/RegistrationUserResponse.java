package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUserResponse {
    private Integer id;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String login;

    private String password;
}
