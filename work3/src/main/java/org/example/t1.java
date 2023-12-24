package org.example;

import org.example.service.T1service;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class t1 implements CommandLineRunner {
    private final T1service service;

    public t1(T1service service) {
        this.service = service;
    }

    public static void main(String[] args) {
        SpringApplication.run(t1.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        // Чтение данных из JSON и запись в базу данных
     //  service.readDataAndSaveToDatabase();

        // Выполнение запросов
        //yourService.executeQueries();
    }
}