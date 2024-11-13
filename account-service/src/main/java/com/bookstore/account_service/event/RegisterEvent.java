package com.bookstore.account_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEvent {
    private long id;
    private String username;
    private String role;
    private boolean enabled;
}
