package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationAdminResponse extends RegistrationUserResponse {
    private String post;

    public RegistrationAdminResponse(Integer id, String firstName, String lastName, String patronymic, String login,
                                     String password, String token, String post) {
        super(id, firstName, lastName, patronymic, login, password, token);
        this.post = post;
    }
}
