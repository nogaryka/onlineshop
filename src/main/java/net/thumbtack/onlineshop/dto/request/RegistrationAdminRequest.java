package net.thumbtack.onlineshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.onlineshop.validator.annotation.MaxNameLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationAdminRequest extends RegistrationUserRequest {
    @NotBlank
    @Pattern(regexp = "[A-Za-z]+")
    @MaxNameLength
    private String post;

    public RegistrationAdminRequest(String firstName, String lastName, String patronymic, String login, String password, String post) {
        super(firstName, lastName, patronymic, login, password);
        this.post = post;
    }
}