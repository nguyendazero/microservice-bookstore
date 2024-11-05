package com.bookstore.account_service.exception;

import java.util.HashMap;
import java.util.Map;

import com.bookstore.account_service.dto.APICustomize;
import com.bookstore.account_service.dto.ErrorResponse;
import com.bookstore.account_service.enums.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<APICustomize<String>> handleUserExistException(UserExistException ex){
        //Xử lý lỗi khi đăng ký người dùng với username đã tồn tại
        APICustomize<String> response = new APICustomize<String>(ApiError.CONFLICT.getCode(), ex.getMessage(), "Đăng ký thất bại");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ErrorLoginException.class)
    public ResponseEntity<APICustomize<String>> handleErrorLoginException(ErrorLoginException ex) {
        //Xử lý Login notFound with username and password
        APICustomize<String> response = new APICustomize<>(ApiError.BAD_REQUEST.getCode(), ex.getMessage(), "Đăng nhập thất bại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        //Xử lý các lỗi khác
        ErrorResponse errorResponse = new ErrorResponse(
                "An unexpected error occurred chung",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
