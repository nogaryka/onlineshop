package net.thumbtack.onlineshop.dto.request;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditAccountAdminRequest {
    @NotBlank
    @Pattern(regexp = "[A-Za-z]+")
    @Size(min = 2, max = 20)
    private String firstName;

    @NotBlank
    @Pattern(regexp = "[A-Za-z]+")
    @Size(min = 2, max = 30)
    private String lastName;

    @Pattern(regexp = "[A-Za-z]+")
    @Size(max = 30)
    private String patronymic;

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9]+")
    @Size(min = 6, max = 20)
    private String password;

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9]+")
    @Size(min = 6, max = 20)
    private String newPassword;

    @NotBlank
    @Pattern(regexp = "[A-Za-z]+")
    @Size(min = 2, max = 15)
    private String post;

}
