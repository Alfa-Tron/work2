package com.example.work4.service.task2;

import com.example.work4.model.BuildingInfo;
import com.example.work4.repository.T1repository;
import com.example.work4.repository.T2repository;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class T2serviceImpl implements T2service {
    private final T2repository t2repository;
    private final T1repository repository1;
    @Override
    @Transactional
    public void readDataAndSaveToDatabase() {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder().setUseHeader(true).setColumnSeparator(';').build();
        ObjectReader reader = csvMapper.readerFor(BuildingInfo.class).with(schema);

        try {
            File file = new File("src/main/java/com/example/work4/task2RES/task2.csv");
            MappingIterator<BuildingInfo> buildingIterator = reader.readValues(file);

            List<BuildingInfo> buildings = buildingIterator.readAll();
            for(BuildingInfo buildingInfo : buildings){
                if(!buildingInfo.getCity().getName().equals("")){
                String name = buildingInfo.getCity().getName();
                BuildingInfo existingCity = t2repository.findByCityName(name);
                if(existingCity ==null){
                    buildingInfo.setCity(repository1.findByName(name));
                    t2repository.save(buildingInfo);
                }}

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void first() {
        Optional<BuildingInfo> floorsAggregationResult = t2repository.findById(3L);


        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        try {

            File file = new File("src/main/java/com/example/work4/task2RES/first.json");
            objectWriter.writeValue(file, floorsAggregationResult.get());
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void second() {
        Optional<BuildingInfo> floorsAggregationResult = t2repository.findById(10L);


        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        try {

            File file = new File("src/main/java/com/example/work4/task2RES/ses.json");
            objectWriter.writeValue(file, floorsAggregationResult.get());
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void th() {
        List<BuildingInfo> floorsAggregationResult = t2repository.findAll();


        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        try {

            File file = new File("src/main/java/com/example/work4/task2RES/th.json");
            objectWriter.writeValue(file, floorsAggregationResult);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}

