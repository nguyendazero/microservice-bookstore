package com.bookstore.notification_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
    @KafkaListener(topics = "add-book")
    public void notificationAddBook(AddBookEvent addBookEvent){
        //send out a mail notification
        log.info("Received Notification for add book - {}", addBookEvent.toString());
    }

    @KafkaListener(topics = "register")
    public void notificationRegister(RegisterEvent registerEvent){
        //send out a mail notification
        log.info("Received Notification for register - {}", registerEvent.toString());
    }
}
