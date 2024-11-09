package com.bookstore.account_service.dto;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String username;

    private List<String> roles;

    private String jwtToken;

    private String refreshToken;
}
