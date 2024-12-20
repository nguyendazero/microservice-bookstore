package com.bookstore.account_service.service;

import com.bookstore.account_service.dto.*;
import com.bookstore.account_service.enums.ApiError;
import com.bookstore.account_service.event.RegisterEvent;
import com.bookstore.account_service.exception.ErrorLoginException;
import com.bookstore.account_service.exception.UserExistException;
import com.bookstore.account_service.model.Account;
import com.bookstore.account_service.repository.AccountRepository;
import com.bookstore.account_service.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final KafkaTemplate<String, RegisterEvent> kafkaTemplate;

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
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Lấy danh sách roles từ authorities
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Tạo JWT token với roles
            String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

            String refreshToken = jwtUtils.generateRefreshTokenFromUsername(userDetails);

            LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken, refreshToken);

            return new APICustomize<>(ApiError.OK.getCode(), ApiError.OK.getMessage(), response);
        } catch (BadCredentialsException e) {
            throw new ErrorLoginException("Sai tên đăng nhập hoặc mật khẩu!");
        }
    }


    public APICustomize<RegisterResponse> register(RegisterRequest registerRequest) {

        if (accountRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserExistException("Tên đăng nhập đã tồn tại!");
        }

        Account newUser = new Account();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setEnabled(true);
        newUser.setRole("ROLE_USER");

        accountRepository.save(newUser);

        kafkaTemplate.send("register", new RegisterEvent(newUser.getId(), newUser.getUsername(), newUser.getRole(), newUser.isEnabled()));

        RegisterResponse response = new RegisterResponse(
                newUser.getUsername(),
                newUser.getPassword(),
                "Đăng ký thành công"
        );

        return new APICustomize<>(ApiError.CREATED.getCode(), ApiError.CREATED.getMessage(), response);
    }

    public APICustomize<String> deleteAccount(long id){
        accountRepository.deleteById(id);
        return new APICustomize<>(ApiError.NO_CONTENT.getCode(), ApiError.NO_CONTENT.getMessage(), "Đã xóa thành công account with id = " + id);
    }
}