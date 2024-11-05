package com.bookstore.account_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountsReponse {
    public String username;
    public String password;
    public String role;
    public boolean enabled;
}
