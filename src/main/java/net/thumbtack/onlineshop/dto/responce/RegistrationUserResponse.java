package net.thumbtack.onlineshop.dto.responce;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @JsonIgnore
    private String token;
}
