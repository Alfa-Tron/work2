package org.example.w4;

import lombok.Data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.w1.StatisticsResult;
import org.example.w2.Ex2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ex4 {
    public static void main(String[] args) {
        String folderPath = "zip_var_13333333333";

        File folder = new File(folderPath);

        File[] listOfFiles = folder.listFiles();
        List<Clothing> clothing = new ArrayList<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".xml")) {
                    clothing.addAll(parseXml(file.getAbsolutePath()));

                }
            }
        }
        writeToJson(clothing, "w4All");
        sortByYear(clothing);
        writeToJson(clothing, "w4Sort1");
        sortByName(clothing);
        writeToJson(clothing, "w4Sort2");
        StatisticsResult statisticsResult = calculateStatistics(clothing, "price");

        Gson gson = new Gson();
        String jsonResult = gson.toJson(statisticsResult);
        try (FileWriter fileWriter = new FileWriter("w4statistics_result.json")) {
            fileWriter.write(jsonResult);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static StatisticsResult calculateStatistics(List<Clothing> clothing, String numericField) {
        List<Integer> values = new ArrayList<>();
        for (Clothing product : clothing) {
            switch (numericField) {
                case "price":
                    values.add((int) product.getPrice());
                    break;
            }
        }

        int sum = values.stream().mapToInt(Integer::intValue).sum();
        int min = values.stream().min(Comparator.naturalOrder()).orElse(0);
        int max = values.stream().max(Comparator.naturalOrder()).orElse(0);
        double average = values.stream().mapToInt(Integer::intValue).average().orElse(0);

        return new StatisticsResult(sum, min, max, average);
    }

    private static void sortByYear(List<Clothing> clothing) {
        clothing.sort(Comparator.comparingDouble(Clothing::getPrice));
    }

    private static void sortByName(List<Clothing> clothing) {
        clothing.sort(Comparator.comparing(Clothing::getName));
    }

    private static List<Clothing> parseXml(String xmlFilePath) {
        List<Clothing> clothingList = new ArrayList<>();

        try {
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("clothing");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                Clothing clothing = new Clothing();
                clothing.setId(Long.parseLong(getTagValue("id", element)));
                clothing.setName(getTagValue("name", element));
                clothing.setCategory(getTagValue("category", element));
                clothing.setSize(getTagValue("size", element));
                clothing.setColor(getTagValue("color", element));
                clothing.setMaterial(getTagValue("material", element));
                clothing.setPrice(Long.parseLong(getTagValue("price", element)));
                clothing.setRating(Double.parseDouble(getTagValue("rating", element)));
                clothing.setReviews(Long.parseLong(getTagValue("reviews", element)));
                clothing.setIsNew(getTagValue("new", element));
                clothing.setExclusive(getTagValue("exclusive", element));
                clothing.setSporty(getTagValue("sporty", element));

                clothingList.add(clothing);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clothingList;
    }

    private static String getTagValue(String tagName, Element element) {
        try {
            NodeList nodeList = element.getElementsByTagName(tagName).item(0).getChildNodes();
            return nodeList.item(0).getNodeValue().trim();
        }catch (Exception e){
            return "0";
        }
    }

    private static void writeToJson(List<Clothing> clothing, String outputPath) {
        try (FileWriter fileWriter = new FileWriter(outputPath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(clothing, fileWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Data
    static class Clothing {
        private long id;
        private String name;
        private String category;
        private String size;
        private String color;
        private String material;
        private long price;
        private double rating;
        private long reviews;
        private String isNew;
        private String exclusive;
        private String sporty;
    }
}