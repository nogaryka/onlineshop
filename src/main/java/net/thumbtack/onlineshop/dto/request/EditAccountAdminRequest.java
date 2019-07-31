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
public class EditAccountAdminRequest {
    @NotBlank
    @MaxNameLength
    @Pattern(regexp = "[А-Яа-яA-Za-z \\-]+")
    private String firstName;

    @NotBlank
    @MaxNameLength
    @Pattern(regexp = "[А-Яа-яA-Za-z \\-]+")
    private String lastName;

    @MaxNameLength
    @Pattern(regexp = "[А-Яа-яA-Za-z \\-]+")
    private String patronymic;

    @MinPasswordLength
    private String password;

    @MinPasswordLength
    private String newPassword;

    @NotBlank
    @Pattern(regexp = "[А-Яа-яA-Za-z0-9 ]+")
    private String post;
}
