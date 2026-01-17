package com.shopix.oder.dto;

import lombok.Data;

@Data
public class UserView {
    private String id;
    private String keycloakId;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isSeller;
}
