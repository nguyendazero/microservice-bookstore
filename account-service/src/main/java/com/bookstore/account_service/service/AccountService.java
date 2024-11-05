package com.bookstore.account_service.service;

import com.bookstore.account_service.dto.*;
import com.bookstore.account_service.enums.ApiError;
import com.bookstore.account_service.model.Account;
import com.bookstore.account_service.repository.AccountRepository;
import com.bookstore.account_service.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public APICustomize<List<AccountsReponse>> accounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountsReponse> accountsResponses = accounts.stream()
                .map(account -> new AccountsReponse(
                        account.getUsername(),
                        account.getPassword(),
                        account.getRole(),
                        account.isEnabled()
                ))
                .toList();

        return new APICustomize<>(ApiError.OK.getCode(), ApiError.OK.getMessage(), accountsResponses);
    }

    public APICustomize<LoginResponse> authenticateUser(LoginRequest loginRequest) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

            String refreshToken = jwtUtils.generateRefreshTokenFromUsername(userDetails);

            LoginResponse response = new LoginResponse(userDetails.getUsername(), jwtToken, refreshToken);

            return new APICustomize<>(ApiError.OK.getCode(), ApiError.OK.getMessage(), response);

    }

    public APICustomize<RegisterResponse> register(RegisterRequest registerRequest) {

//        if (accountRepository.existsByUsername(registerRequest.getUsername())) {
//            throw new UserExistException("Tên đăng nhập đã tồn tại!");
//        }

        Account newUser = new Account();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setEnabled(true);
        newUser.setRole("ROLE_USER");

        accountRepository.save(newUser);

        RegisterResponse response = new RegisterResponse(
                newUser.getUsername(),
                newUser.getPassword(),
                "Đăng ký thành công"
        );

        return new APICustomize<>(ApiError.CREATED.getCode(), ApiError.CREATED.getMessage(), response);
    }
}
