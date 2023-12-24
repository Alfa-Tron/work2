package com.example.work4.service.task1;

import com.example.work4.model.City;
import com.example.work4.repository.T1repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class T1serviceImpl implements T1service{
    private  final T1repository repository;
    @Override
    public void readDataAndSaveToDatabase() {
        ObjectMapper objectMapper = new ObjectMapper();
            try {
                City[] buildingsArray = objectMapper.readValue(new File("src/main/java/com/example/work4/task1RES/task1.json"), City[].class);
                List<City> buildings = Arrays.asList(buildingsArray);

                repository.saveAll(buildings);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    @Override//вывод 23 записи отсортированных по floors
    public void sort1var13() {
        List<City> cities = repository.findFirst23ByOrderByFloors();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        try {

            File file = new File("src/main/java/com/example/work4/task1RES/t1sort1.json");
            objectWriter.writeValue(file, cities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printAggregate() {
        Map<String, Object> floorsAggregationResult = repository.aggregateFloors();


        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        try {

            File file = new File("src/main/java/com/example/work4/task1RES/floorsAggregationResult.json");
            objectWriter.writeValue(file, floorsAggregationResult);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void categoryPole() {
        List<Map<String, Object>> parkingStats = repository.countParking();
        System.out.println("parc False : "+ parkingStats.get(0).get("count"));//47

        System.out.println("parc True : "+ parkingStats.get(1).get("count"));//50

    }

    @Override
    public void saveFilteredAndSortedToJson() {
        List<City> c = repository.findFirst23ByYearGreaterThanOrderByFloors(1900);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter(); // Используйте PrettyPrinter
        try {

            File file = new File("src/main/java/com/example/work4/task1RES/filterAndSortT1.json");
            objectWriter.writeValue(file, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



