package com.bookstore.book_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBookEvent {
    private long id;
    private String name;
    private String description;
    private int quantity;
}
