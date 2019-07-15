package net.thumbtack.onlineshop.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformarionAdoutClientsForAdminResponse {
    private Integer id;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String email;

    private String postalAddress;

    private String phoneNumber;

    private final String userType = "client";
}
