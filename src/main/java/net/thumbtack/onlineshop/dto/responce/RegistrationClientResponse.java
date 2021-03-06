package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationClientResponse extends RegistrationUserResponse {
    private String email;

    private String postalAddress;

    private String phoneNumber;

    private Integer cash;

    public RegistrationClientResponse(Integer id, String firstName, String lastName, String patronymic, String login,
                                      String password, String token, String email, String phoneNumber,
                                      String postalAddress, Integer cash) {
        super(id, firstName, lastName, patronymic, login, password, token);
        this.email = email;
        this.postalAddress = postalAddress;
        this.phoneNumber = phoneNumber;
        this.cash = cash;
    }

    public RegistrationClientResponse(Integer id, String firstName, String lastName, String patronymic, String login, String password, String cookie, String email, String phoneNumber, String postalAddress) {
        super(id, firstName, lastName, patronymic, login, password, cookie);
        this.email = email;
        this.postalAddress = postalAddress;
        this.phoneNumber = phoneNumber;
    }
}
