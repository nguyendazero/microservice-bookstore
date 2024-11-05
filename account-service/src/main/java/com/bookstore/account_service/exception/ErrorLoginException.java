package com.bookstore.account_service.exception;

public class ErrorLoginException extends RuntimeException{

    public ErrorLoginException(String message) {
        super(message);
    }

}
