package com.bookstore.account_service.controller;

import com.bookstore.account_service.dto.*;
import com.bookstore.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Received login request for username: {}", loginRequest.getUsername());
        APICustomize<LoginResponse> response = accountService.authenticateUser(loginRequest);
        logger.info("Login request processed successfully for username: {}", loginRequest.getUsername());
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        APICustomize<RegisterResponse> response = accountService.register(registerRequest);
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

    @GetMapping("/admin/accounts")
    public ResponseEntity<List<AccountsReponse>> accounts(){
        APICustomize<List<AccountsReponse>> response = accountService.accounts();
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response.getResult());
    }

}