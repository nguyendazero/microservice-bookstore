package com.bookstore.book_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APICustomize<T> {
    private String statusCode;
    private  String message;
    private T result ;

}
