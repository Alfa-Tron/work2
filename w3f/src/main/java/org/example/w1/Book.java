package org.example.w1;

import lombok.Data;

@Data
public class Book {
        private String category;
        private String title;
        private String author;
        private int pages;
        private int year;
        private String isbn;
        private String description;
        private double rating;
        private int views;

        // Добавьте конструктор и геттеры/сеттеры по необходимости

        // Пример конструктора
        public Book() {
            // Конструктор по умолчанию
        }
    }


