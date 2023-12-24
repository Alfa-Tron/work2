package org.example.t1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class w1Andw2Andw3 {
    public static void main(String[] args) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("your_database_name");


            List<String> lines = Files.readAllLines(Paths.get("src/main/java/org/example/t1/task1.json"));
            StringBuilder jsonContent = new StringBuilder();
            lines.forEach(jsonContent::append);
            ObjectMapper objectMapper = new ObjectMapper();

            List<Document> documents = objectMapper.readValue(jsonContent.toString(), new TypeReference<List<Document>>() {});

            MongoCollection<Document> collection = database.getCollection("your_collection_name");
            collection.drop();
            collection.insertMany(documents);
            FindIterable<Document> sortedDocuments = collection.find().sort(Sorts.descending("salary")).limit(10);

            List<String> jsonList = new ArrayList<>();
            for (Document document : sortedDocuments) {
                jsonList.add(document.toJson());
            }
            writeJsonToFile(jsonList, "src/main/java/org/example/t1/top10salary");

            FindIterable<Document> filteredAndSortedDocuments = collection.find(Filters.lt("age", 30))
                    .sort(Sorts.descending("salary")).limit(15);
            jsonList = new ArrayList<>();
            for (Document document : filteredAndSortedDocuments) {
                jsonList.add(document.toJson());
            }


            writeJsonToFile(jsonList, "src/main/java/org/example/t1/age<30");
            String targetCity = "Сория";
            List<String> targetProfessions = List.of("Инженер", "Учитель", "Психолог");

            FindIterable<Document> filteredAndSortedDocumentsHard = collection.find(
                    Filters.and(
                            Filters.eq("city", targetCity),
                            Filters.in("job", targetProfessions)
                    )
            ).sort(Sorts.ascending("age")).limit(10);
            jsonList = new ArrayList<>();
            for (Document document : filteredAndSortedDocumentsHard) {
                jsonList.add(document.toJson());
            }
            writeJsonToFile(jsonList, "src/main/java/org/example/t1/hardpredicate");


            int minAge = 25;
            int maxAge = 35;
            int minYear = 2019;
            int maxYear = 2022;
            int minSalaryRange1 = 50000;
            int maxSalaryRange1 = 75000;
            int minSalaryRange2 = 125000;
            int maxSalaryRange2 = 150000;

            FindIterable<Document> filteredDocuments = collection.find(
                    Filters.and(
                            Filters.gte("age", minAge),
                            Filters.lte("age", maxAge),
                            Filters.gte("year", minYear),
                            Filters.lte("year", maxYear),
                            Filters.or(
                                    Filters.and(
                                            Filters.gt("salary", minSalaryRange1),
                                            Filters.lte("salary", maxSalaryRange1)
                                    ),
                                    Filters.and(
                                            Filters.gt("salary", minSalaryRange2),
                                            Filters.lt("salary", maxSalaryRange2)
                                    )
                            )
                    )
            );
            jsonList = new ArrayList<>();
            for (Document document : filteredDocuments) {
                jsonList.add(document.toJson());
            }
            writeJsonToFile(jsonList, "src/main/java/org/example/t1/ageYearSalary");








            /////222222















            List<String> lines1 = Files.readAllLines(Paths.get("src/main/java/org/example/t2/task_2_item.json"));
            StringBuilder jsonContent1 = new StringBuilder();
            lines1.forEach(jsonContent1::append);

            List<Document> documents1 = objectMapper.readValue(jsonContent1.toString(), new TypeReference<List<Document>>() {});

            for (Document document : documents1) {
                Document existingDocument = collection.find(Filters.eq("_id", document.get("_id"))).first();
                if (existingDocument == null) {
                    collection.insertOne(document);
                }
            }


            AggregateIterable<Document> result = collection.aggregate(List.of(
                    new Document("$group", new Document("_id", null)
                            .append("minSalary", new Document("$min", "$salary"))
                            .append("avgSalary", new Document("$avg", "$salary"))
                            .append("maxSalary", new Document("$max", "$salary"))
                    )
            ));

            Document statistics = result.first();


            try (FileWriter writer = new FileWriter("src/main/java/org/example/t2/Salary")) {
                writer.write(statistics.toJson());
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Document> pipeline = Arrays.asList(
                    new Document("$group", new Document("_id", "$job").append("count", new Document("$sum", 1)))
            );
            AggregateIterable<Document> result1 = collection.aggregate(pipeline);

            try (FileWriter fileWriter = new FileWriter("src/main/java/org/example/t2/profes")) {
                List<Document> resultDocuments = new ArrayList<>();
                for (Document document : result1) {
                    resultDocuments.add(document);
                }
                fileWriter.write(resultDocuments.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }


            List<Document> pipeline1 = Arrays.asList(
                    new Document("$group", new Document("_id", "$city")
                            .append("minSalary", new Document("$min", "$salary"))
                            .append("avgSalary", new Document("$avg", "$salary"))
                            .append("maxSalary", new Document("$max", "$salary")))
            );

            AggregateIterable<Document> result2 = collection.aggregate(pipeline1);

            try (FileWriter fileWriter = new FileWriter("src/main/java/org/example/t2/salaryByCity")) {
                List<Document> resultDocuments = new ArrayList<>();
                for (Document document : result2) {
                    resultDocuments.add(document);
                }
                fileWriter.write(resultDocuments.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }


            List<Document> pipeline3 = Arrays.asList(
                    new Document("$group", new Document("_id", "$job")
                            .append("minSalary", new Document("$min", "$salary"))
                            .append("avgSalary", new Document("$avg", "$salary"))
                            .append("maxSalary", new Document("$max", "$salary")))
            );

            AggregateIterable<Document> result3 = collection.aggregate(pipeline3);

            try (FileWriter fileWriter = new FileWriter("src/main/java/org/example/t2/salaryByProf")) {
                List<Document> resultDocuments = new ArrayList<>();
                for (Document document : result3) {
                    resultDocuments.add(document);
                }
                fileWriter.write(resultDocuments.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }




            List<Document> pipeline4 = Arrays.asList(
                    new Document("$group", new Document("_id", "$city")
                            .append("minAge", new Document("$min", "$age"))
                            .append("avgAge", new Document("$avg", "$age"))
                            .append("maxAge", new Document("$max", "$age")))
            );

            AggregateIterable<Document> result4 = collection.aggregate(pipeline4);

            try (FileWriter fileWriter = new FileWriter("src/main/java/org/example/t2/ageBytown")) {
                List<Document> resultDocuments = new ArrayList<>();
                for (Document document : result4) {
                    resultDocuments.add(document);
                }
                fileWriter.write(resultDocuments.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }



            List<Document> pipeline5 = Arrays.asList(
                    new Document("$group", new Document("_id", "$job")
                            .append("minAge", new Document("$min", "$age"))
                            .append("avgAge", new Document("$avg", "$age"))
                            .append("maxAge", new Document("$max", "$age")))
            );

            AggregateIterable<Document> result5 = collection.aggregate(pipeline5);

            try (FileWriter fileWriter = new FileWriter("src/main/java/org/example/t2/agebyPROF")) {
                List<Document> resultDocuments = new ArrayList<>();
                for (Document document : result5) {
                    resultDocuments.add(document);
                }
                fileWriter.write(resultDocuments.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }



            List<Document> pipeline6 = Arrays.asList(
                    new Document("$group", new Document("_id", "$age")
                            .append("minSalary", new Document("$min", "$salary"))),
                    new Document("$sort", new Document("_id", 1)),
                    new Document("$limit", 1)
            );

            AggregateIterable<Document> result6 = collection.aggregate(pipeline6);

            try (FileWriter fileWriter = new FileWriter("src/main/java/org/example/t2/max+minAGE")) {
                List<Document> resultDocuments = new ArrayList<>();
                for (Document document : result6) {
                    resultDocuments.add(document);
                }
                fileWriter.write(resultDocuments.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }



            List<Document> pipeline7 = Arrays.asList(
                    new Document("$match", new Document("salary", new Document("$gt", 50000))),
                    new Document("$group", new Document("_id", "$city")
                            .append("minAge", new Document("$min", "$age"))
                            .append("avgAge", new Document("$avg", "$age"))
                            .append("maxAge", new Document("$max", "$age"))),
                    new Document("$sort", new Document("your_sort_field", 1))
            );

            AggregateIterable<Document> result7 = collection.aggregate(pipeline7);

            try (FileWriter fileWriter = new FileWriter("src/main/java/org/example/t2/age_statistics_by_city_with_salary_filter")) {
                List<Document> resultDocuments = new ArrayList<>();
                for (Document document : result7) {
                    resultDocuments.add(document);
                }
                fileWriter.write(resultDocuments.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }




            List<Document> pipeline8 = Arrays.asList(
                    new Document("$match", new Document("$or", Arrays.asList(
                            new Document("$and", Arrays.asList(
                                    new Document("age", new Document("$gt", 18).append("$lt", 25)),
                                    new Document("city", "your_city"),
                                    new Document("job", "your_job")
                            )),
                            new Document("$and", Arrays.asList(
                                    new Document("age", new Document("$gt", 50).append("$lt", 65)),
                                    new Document("city", "your_city"),
                                    new Document("job", "your_job")
                            ))
                    ))),
                    new Document("$group", new Document("_id", null)
                            .append("minSalary", new Document("$min", "$salary"))
                            .append("avgSalary", new Document("$avg", "$salary"))
                            .append("maxSalary", new Document("$max", "$salary")))
            );

            AggregateIterable<Document> result8 = collection.aggregate(pipeline8);

            try (FileWriter fileWriter = new FileWriter("src/main/java/org/example/t2/salary_statistics_by_conditions")) {
                List<Document> resultDocuments = new ArrayList<>();
                for (Document document : result8) {
                    resultDocuments.add(document);
                }
                fileWriter.write(resultDocuments.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }



            List<Document> pipeline9 = Arrays.asList(
                    new Document("$match", new Document("your_filter_field", "your_filter_value")),
                    new Document("$group", new Document("_id", "$job")
                            .append("avgSalary", new Document("$avg", "$salary"))),
                    new Document("$sort", new Document("avgSalary", -1))
            );

            AggregateIterable<Document> result9 = collection.aggregate(pipeline9);
            try (FileWriter fileWriter = new FileWriter("src/main/java/org/example/t2/sortmatchgroup")) {
                List<Document> resultDocuments = new ArrayList<>();
                for (Document document : result9) {
                    resultDocuments.add(document);
                }
                fileWriter.write(resultDocuments.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }










            /////3












            String csvFilePath = "src/main/java/org/example/t3/task_3_item.csv";

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(";");

                    if(!values[0].equals("job")) {
                        Document document = new Document()
                                .append("job", values[0])
                                .append("salary", Integer.parseInt(values[1]))
                                .append("id", Long.parseLong(values[2]))
                                .append("city", values[3])
                                .append("year", Integer.parseInt(values[4]))
                                .append("age", Integer.parseInt(values[5]));
                        collection.insertOne(document);
                    }
                }}

            Document deleteFilter = new Document("$or",
                    List.of(
                            new Document("salary", new Document("$lt", 25000)),
                            new Document("salary", new Document("$gt", 175000))
                    )
            );

            // Увеличение возраста на 1 для всех документов
            collection.updateMany(new Document(), Updates.inc("age", 1));

            String[] targetPr = {"Инженер", "Учитель"};

            for (String profession : targetPr) {
                collection.updateMany(
                        Filters.eq("job", profession),
                        Updates.mul("salary", 1.05)
                );
            }

            // Увеличение заработной платы на 7% для выбранных городов
            String[] targetCities = {"Барселона", "Тбилиси"};

            for (String city : targetCities) {
                collection.updateMany(
                        Filters.eq("city", city),
                        Updates.mul("salary", 1.07)
                );
            }
            Bson complexPredicate = Filters.and(
                    Filters.eq("city", "Тарраса"),
                    Filters.in("job", "Водитель", "Повар", "Косметолог"),
                    Filters.gte("age", 25)
            );

            // Увеличение заработной платы на 10% для выбранной выборки
            collection.updateMany(complexPredicate, Updates.mul("salary", 1.10));

            Bson predicate = Filters.and(
                    Filters.eq("city", "Тарраса"),
                    Filters.gte("age", 25)
            );

            // Удаление записей по произвольному предикату
            collection.deleteMany(predicate);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeJsonToFile(List<String> jsonList, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String json : jsonList) {
                writer.write(json + "\n");
            }
        }
    }
    }
