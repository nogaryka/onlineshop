package net.thumbtack.onlineshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.onlineshop.validator.annotation.MaxNameLength;
import net.thumbtack.onlineshop.validator.annotation.MinPasswordLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class RegistrationUserRequest {
    @Pattern(regexp = "[А-Яа-яA-Za-z \\-]+")
    @NotBlank
    @MaxNameLength
    private String firstName;

    @Pattern(regexp = "[А-Яа-яA-Za-z \\-]+")
    @NotBlank
    @MaxNameLength
    private String lastName;

    @Pattern(regexp = "[А-Яа-яA-Za-z \\-]+")
    @MaxNameLength
    private String patronymic;

    @Pattern(regexp = "[А-Яа-яA-Za-z0-9]+")
    @NotBlank
    @MaxNameLength
    private String login;

    @MaxNameLength
    @MinPasswordLength
    private String password;

    public RegistrationUserRequest(String firstName, String lastName, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
    }
}