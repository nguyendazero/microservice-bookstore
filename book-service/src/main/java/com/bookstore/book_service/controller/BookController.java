package com.bookstore.book_service.controller;

import com.bookstore.book_service.dto.APICustomize;
import com.bookstore.book_service.dto.AddBookRequest;
import com.bookstore.book_service.dto.AddBookResponse;
import com.bookstore.book_service.dto.BookResponse;
import com.bookstore.book_service.model.Book;
import com.bookstore.book_service.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/admin/add")
    public ResponseEntity<?> createBook(@RequestBody AddBookRequest request){
        APICustomize<AddBookResponse> response = bookService.create(request);
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

    @GetMapping("/public/books")
    public ResponseEntity<?> books (){
        APICustomize<List<BookResponse>> response = bookService.books();
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

}
