package com.example.work4;

import com.example.work4.service.task1.T1service;
import com.example.work4.service.task2.T2service;
import com.example.work4.service.task3.T3service;
import com.example.work4.service.task4.T4service;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@RequiredArgsConstructor
public class Work4Application implements CommandLineRunner {
    private final T1service servicet1;
    private final T2service servicet2;
    private final T3service servicet3;
    private final T4service servicet4;


    public static void main(String[] args) {
        SpringApplication.run(Work4Application.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        servicet1.readDataAndSaveToDatabase();
        servicet1.sort1var13();//1
        servicet1.printAggregate();//2
        servicet1.categoryPole();//3
        servicet1.saveFilteredAndSortedToJson();//4

        servicet2.readDataAndSaveToDatabase();
        servicet2.first();//1
        servicet2.second();//2
        servicet2.th();//3

        servicet3.readDataAndSaveToDatabase();
        servicet3.print1();
        servicet3.print2();
        servicet3.print3();
        servicet3.print4();

        servicet4.readDataAndSaveToDatabase();
        servicet4.redactData();
        servicet4.print1();
        servicet4.print2();
        servicet4.print3();

    }
}