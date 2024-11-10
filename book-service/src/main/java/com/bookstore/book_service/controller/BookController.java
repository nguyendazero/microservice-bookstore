package com.bookstore.book_service.controller;

import com.bookstore.book_service.dto.APICustomize;
import com.bookstore.book_service.dto.BookResponse;
import com.bookstore.book_service.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/public/books")
    public ResponseEntity<?> books (){
        APICustomize<List<BookResponse>> response = bookService.books();
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

    @GetMapping("/admin/hello")
    public ResponseEntity<?> helloAdmin(){
        return ResponseEntity.ok("hello Admin");
    }

    @GetMapping("/user/hello")
    public ResponseEntity<?> helloUser(){
        return ResponseEntity.ok("hello User");
    }

}
