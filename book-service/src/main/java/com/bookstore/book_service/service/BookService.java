package com.bookstore.book_service.service;

import com.bookstore.book_service.dto.APICustomize;
import com.bookstore.book_service.dto.BookResponse;
import com.bookstore.book_service.enums.ApiError;
import com.bookstore.book_service.model.Book;
import com.bookstore.book_service.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public APICustomize<List<BookResponse>> books(){
        List<Book> books = bookRepository.findAll();
        List<BookResponse> bookResponses = books.stream().map(
                book -> new BookResponse(
                        book.getId(),
                        book.getName(),
                        book.getDescription(),
                        book.getQuantity()
                )
        ).toList();

        return new APICustomize<>(ApiError.OK.getCode(), ApiError.OK.getMessage(), bookResponses);
    }
}
