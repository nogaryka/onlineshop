package net.thumbtack.onlineshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.onlineshop.validator.annotation.MaxNameLength;
import net.thumbtack.onlineshop.validator.annotation.MinPasswordLength;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditAccountClientRequest {
    @NotBlank
    @MaxNameLength
    @Pattern(regexp = "[А-Яа-яA-Za-z \\-]+")
    private String firstName;

    @NotBlank
    @MaxNameLength
    @Pattern(regexp =  "[А-Яа-яA-Za-z \\-]+")
    @Size(min = 2, max = 30)
    private String lastName;

    @MaxNameLength
    @Pattern(regexp =  "[А-Яа-яA-Za-z \\-]+")
    @Size(max = 30)
    private String patronymic;

    @MinPasswordLength
    private String password;

    @MinPasswordLength
    private String newPassword;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "[А-Яа-яA-Za-z0-9 ]+")
    @NotBlank
    private String postalAddress;

    @Pattern(regexp = "[+]?[8 7][-]?\\d{3}[-]?\\d{3}[-]?\\d{2}[-]?\\d{2}")
    private String phoneNumber;
}
