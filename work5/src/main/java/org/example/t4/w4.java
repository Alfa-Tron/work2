package org.example.t4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class w4 {
    public static void main(String[] args) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("your_database_name");


            List<String> lines = Files.readAllLines(Paths.get("src/main/java/org/example/t4/Vehicle Prices.json"));
            StringBuilder jsonContent = new StringBuilder();
            lines.forEach(jsonContent::append);
            ObjectMapper objectMapper = new ObjectMapper();

            List<Document> documents = objectMapper.readValue(jsonContent.toString(), new TypeReference<List<Document>>() {
            });

            MongoCollection<Document> collection = database.getCollection("your_collection_name");
            collection.drop();
            collection.insertMany(documents);

            ////выборка

            FindIterable<Document> cars2022 = collection.find(new Document("Year", 2022));
            List<String> jsonList = new ArrayList<>();

            for (Document document : cars2022) {
                jsonList.add(document.toJson());
            }

            String jsonFilePath = "src/main/java/org/example/t4/выборка1";

            try {
                Files.write(Paths.get(jsonFilePath), jsonList);
            } catch (Exception e) {
                e.printStackTrace();
            }


            FindIterable<Document> cars2023 = collection.find(new Document("Year", 2023));
            List<String> jsonList1 = new ArrayList<>();

            for (Document document : cars2023) {
                jsonList1.add(document.toJson());
            }

            String jsonFilePath1 = "src/main/java/org/example/t4/выборка2";

            try {
                Files.write(Paths.get(jsonFilePath1), jsonList1);
            } catch (Exception e) {
                e.printStackTrace();
            }


            FindIterable<Document> Brand = collection.find(new Document("Brand", "GWM"));
            List<String> jsonList3 = new ArrayList<>();

            for (Document document : Brand) {
                jsonList3.add(document.toJson());
            }

            String jsonFilePath3 = "src/main/java/org/example/t4/выборка3";

            try {
                Files.write(Paths.get(jsonFilePath3), jsonList3);
            } catch (Exception e) {
                e.printStackTrace();
            }

            FindIterable<Document> Brand2 = collection.find(new Document("Brand", "MG"));
            List<String> jsonList4 = new ArrayList<>();

            for (Document document : Brand2) {
                jsonList4.add(document.toJson());
            }

            String jsonFilePath4 = "src/main/java/org/example/t4/выборка4";

            try {
                Files.write(Paths.get(jsonFilePath4), jsonList4);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FindIterable<Document> Brand3 = collection.find(new Document("Brand", "Toyota"));
            List<String> jsonList5 = new ArrayList<>();

            for (Document document : Brand3) {
                jsonList4.add(document.toJson());
            }

            String jsonFilePath5 = "src/main/java/org/example/t4/выборка5";

            try {
                Files.write(Paths.get(jsonFilePath5), jsonList5);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ////22222




            List<Document> pipeline1 = Arrays.asList(
                    new Document("$group", new Document("_id", "$Year")
                            .append("minSalary", new Document("$min", "$Price"))
                            .append("avgSalary", new Document("$avg", "$Price"))
                            .append("maxSalary", new Document("$max", "$Price")))
            );

            AggregateIterable<Document> result = collection.aggregate(pipeline1);

            List<String> jsonListt = new ArrayList<>();
            for (Document document : result) {
                jsonListt.add(document.toJson());
            }

            String jsonFilePatht = "src/main/java/org/example/t4/2выборка1";
            Files.write(Paths.get(jsonFilePatht), jsonListt);




            List<Document> pipeline2 = Arrays.asList(
                    new Document("$group", new Document("_id", "$Brand")
                            .append("count", new Document("$sum", 1)))
            );

             result = collection.aggregate(pipeline2);

             jsonListt = new ArrayList<>();
            for (Document document : result) {
                jsonListt.add(document.toJson());
            }

             jsonFilePatht = "src/main/java/org/example/t4/2выборка2";
            Files.write(Paths.get(jsonFilePatht), jsonListt);




            List<Document> pipeline3 = Arrays.asList(
                    new Document("$group", new Document("_id", "$Location")
                            .append("avgPrice", new Document("$avg", "$Price")))
            );

            result = collection.aggregate(pipeline3);

            jsonListt = new ArrayList<>();
            for (Document document : result) {
                jsonListt.add(document.toJson());
            }

            jsonFilePatht = "src/main/java/org/example/t4/2выборка3";
            Files.write(Paths.get(jsonFilePatht), jsonListt);




            List<Document> pipeline4 = Arrays.asList(
                    new Document("$group", new Document("_id", "$'Car/Suv'")
                            .append("count", new Document("$sum", 1))),
                    new Document("$sort", new Document("count", -1)),
                    new Document("$limit", 1)
            );

            result = collection.aggregate(pipeline4);

            jsonListt = new ArrayList<>();
            for (Document document : result) {
                jsonListt.add(document.toJson());
            }

            jsonFilePatht = "src/main/java/org/example/t4/2выборка4";
            Files.write(Paths.get(jsonFilePatht), jsonListt);
            List<Document> pipeline5 = Arrays.asList(
                    new Document("$group", new Document("_id", "$'Car/Suv'")
                            .append("count", new Document("$sum", 1))),
                    new Document("$sort", new Document("count", -1)),
                    new Document("$limit", 3)
            );
            result = collection.aggregate(pipeline5);

            jsonListt = new ArrayList<>();
            for (Document document : result) {
                jsonListt.add(document.toJson());
            }



            jsonFilePatht = "src/main/java/org/example/t4/2выборка5";
            Files.write(Paths.get(jsonFilePatht), jsonListt);

            ///333333333

            Bson deleteFilter1 = Filters.lt("Year", 2020);
            collection.deleteMany(deleteFilter1);
            Bson deleteFilter2 = Filters.gt("Price", 70000);
            collection.deleteMany(deleteFilter2);
            Bson deleteFilter3 = Filters.eq("Car/Suv", "SUV");
            collection.deleteMany(deleteFilter3);
            Bson deleteFilter4 = Filters.lt("Kilometres", 10000);
            collection.deleteMany(deleteFilter4);
            Bson deleteFilter5 = Filters.eq("UsedOrNew", "USED");
            collection.deleteMany(deleteFilter5);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }}