package com.example.work4.service.task3;

import com.example.work4.model.City;
import com.example.work4.model.Song;
import com.example.work4.repository.T3repository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class T3serviceImpl implements T3service {
    private final T3repository repository;

    @Override
    public void readDataAndSaveToDatabase() {
        try {
            String filePath = "src/main/java/com/example/work4/task3RES/task_3_var_13_part_2.msgpack";
            FileInputStream fis = new FileInputStream(filePath);
            MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(fis);

            Map<String, Song> songsMsg = new TreeMap<>();


            StringBuilder result = new StringBuilder();
            while (unpacker.hasNext()) {
                result.append(unpacker.unpackValue().toString());
                result.append(" ");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result.toString().trim());
            for (int i = 0; i < jsonNode.size(); i++) {
                JsonNode j = jsonNode.get(i);
                Song song = new Song();
                song.setArtist(j.get("artist").asText());
                song.setSong(j.get("song").asText());
                song.setDuration_ms(j.get("duration_ms").asText());
                song.setYear(Integer.parseInt(j.get("year").asText()));
                song.setTempo(j.get("tempo").asText());
                song.setGenre(j.get("genre").asText());
                song.setMode(j.get("mode").asText());
                song.setSpeechiness(j.get("speechiness").asText());
                song.setAcousticness(j.get("acousticness").asText());
                song.setInstrumentalness(j.get("instrumentalness").asText());
                songsMsg.put(song.getDuration_ms(), song);
            }

            String filePath1 = "src/main/java/com/example/work4/task3RES/task_3_var_13_part_1.text";

            BufferedReader br = new BufferedReader(new FileReader(filePath1));
            String line;
            List<Song> result12 = new ArrayList<>(1000);
            int k = 0;

            String song = null;
            String exp = null;
            String duration = null;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("::");
                if (parts.length == 2) {
                    if (k == 1) {
                        song = parts[1];
                    }else if(k==2){
                        duration=parts[1];
                    }
                    else if (k == 7) {
                        exp = parts[1];
                    } else if (k == 8) {
                        String l = parts[1];
                        Song s = songsMsg.get(duration);
                        if (s != null) {
                            s.setExplicit(exp);
                            s.setLoudness(l);
                            s.setSong1(song);
                            result12.add(s);
                        }
                        k = -2;
                    }
                }

                k++;
            }
            repository.saveAll(result12);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void print1() {

        List<Song> cities = repository.findFirst23ByOrderByYear();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        try {

            File file = new File("src/main/java/com/example/work4/task3RES/1.json");
            objectWriter.writeValue(file, cities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void print2() {
        Map<String, Object> floorsAggregationResult = repository.aggregateFloors();


        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        try {

            File file = new File("src/main/java/com/example/work4/task3RES/floorsAggregationResult.json");
            objectWriter.writeValue(file, floorsAggregationResult);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void print3() {
        List<Map<String, Object>> parkingStats = repository.countMode();
        System.out.println("mode False : "+ parkingStats.get(0).get("count"));//47

        System.out.println("mode True : "+ parkingStats.get(1).get("count"));//47
    }

    @Override
    public void print4() {
        List<Song> c = repository.findFirst28ByYearGreaterThanOrderByTempo(1);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter(); // Используйте PrettyPrinter
        try {

            File file = new File("src/main/java/com/example/work4/task3RES/filterAndSortT.json");
            objectWriter.writeValue(file, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
