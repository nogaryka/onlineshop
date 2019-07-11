package net.thumbtack.onlineshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.onlineshop.validator.annotation.MaxNameLength;
import net.thumbtack.onlineshop.validator.annotation.MinPasswordLength;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class RegistrationUserRequest {
    @NotBlank
    @Pattern(regexp = "[A-Za-z]+")
    @MaxNameLength
    private String firstName;

    @NotBlank
    @Pattern(regexp = "[A-Za-z]+")
    @MaxNameLength
    private String lastName;

    @Pattern(regexp = "[A-Za-z]+")
    @MaxNameLength
    private String patronymic;

    @NotBlank
    @Pattern(regexp = "[А-Яа-яA-Za-z0-9]+")
    @MaxNameLength
    private String login;

    @NotBlank
    @MinPasswordLength
    private String password;
}