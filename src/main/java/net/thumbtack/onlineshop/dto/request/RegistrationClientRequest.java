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
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String postalAddress;

    @Pattern(regexp = "[+]?[8 7][-]?\\d{3}[-]?\\d{3}[-]?\\d{2}[-]?\\d{2}")
    @NotBlank
    private String phoneNumber;

    public RegistrationClientRequest(String firstName, String lastName, String patronymic, String login,
                                     String password, String email, String postalAddress, String phoneNumber) {
        super(firstName, lastName, patronymic, login, password);
        this.email = email;
        this.postalAddress = postalAddress;
        this.phoneNumber = phoneNumber;
    }
}
