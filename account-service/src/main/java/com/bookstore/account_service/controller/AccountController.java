package com.bookstore.account_service.controller;

import com.bookstore.account_service.dto.*;
import com.bookstore.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/api/public/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        APICustomize<LoginResponse> response = accountService.authenticateUser(loginRequest);
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

    @PostMapping("/api/public/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        APICustomize<RegisterResponse> response = accountService.register(registerRequest);
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

    @GetMapping("/api/account/admin/accounts")
    public ResponseEntity<List<AccountsReponse>> accounts(){
        APICustomize<List<AccountsReponse>> response = accountService.accounts();
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response.getResult());
    }

    @DeleteMapping("/api/account/admin/delete/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable long id) {
        APICustomize<String> response = accountService.deleteAccount(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response.getResult());
    }
}