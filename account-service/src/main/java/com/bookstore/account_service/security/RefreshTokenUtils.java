package com.bookstore.account_service.security;

import java.util.UUID;

public class RefreshTokenUtils {

    public static String createRefreshToken() {
        return UUID.randomUUID().toString();
    }
}
