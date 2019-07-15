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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditAccountAdminRequest {
    @NotBlank
    @MaxNameLength
    @Pattern(regexp = "[A-Za-z]+")
    private String firstName;

    @NotBlank
    @MaxNameLength
    @Pattern(regexp = "[A-Za-z]+")
    private String lastName;

    @MaxNameLength
    @Pattern(regexp = "[A-Za-z]+")
    private String patronymic;

    @MinPasswordLength
    @Pattern(regexp = "[A-Za-z0-9]+")
    private String password;

    @MinPasswordLength
    @Pattern(regexp = "[A-Za-z0-9]+")
    private String newPassword;

    @NotBlank
    @Pattern(regexp = "[A-Za-z]+")
    private String post;
}
