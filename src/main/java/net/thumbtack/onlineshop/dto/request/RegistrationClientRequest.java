package net.thumbtack.onlineshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationClientRequest extends RegistrationUserRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String postalAddress;

    @Pattern(regexp = "[+]?[8 7][-]?\\d{3}[-]?\\d{3}[-]?\\d{2}[-]?\\d{2}")
    private String phoneNumber;
}
