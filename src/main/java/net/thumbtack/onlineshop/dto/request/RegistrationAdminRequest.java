package net.thumbtack.onlineshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationAdminRequest extends RegistrationUserRequest {
    @NotBlank
    @Pattern(regexp = "[А-Яа-яA-Za-z0-9 ]+")
    private String post;

    public RegistrationAdminRequest(String firstName, String lastName, String patronymic, String login, String password, String post) {
        super(firstName, lastName, patronymic, login, password);
        this.post = post;
    }
}