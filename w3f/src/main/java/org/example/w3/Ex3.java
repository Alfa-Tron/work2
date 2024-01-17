package org.example.w3;

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

public class Ex3 {
    public static void main(String[] args) {
        String folderPath = "zip_var_13333";

        File folder = new File(folderPath);

        File[] listOfFiles = folder.listFiles();
        List<Star> stars = new ArrayList<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".xml")) {
                    stars.addAll(parseXml(file.getAbsolutePath()));

                }
            }
        }
        writeToJson(stars, "w3All");
        sortByYear(stars);
        writeToJson(stars, "w3Sort1");
        sortByName(stars);
        writeToJson(stars, "w3Sort2");
        StatisticsResult statisticsResult = calculateStatistics(stars, "radius");

        Gson gson = new Gson();
        String jsonResult = gson.toJson(statisticsResult);
        try (FileWriter fileWriter = new FileWriter("w3statistics_result.json")) {
            fileWriter.write(jsonResult);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static StatisticsResult calculateStatistics(List<Star> stars, String numericField) {
        List<Integer> values = new ArrayList<>();
        for (Star product : stars) {
            switch (numericField) {
                case "radius":
                    values.add((int) product.getRadius());
                    break;
            }
        }

        int sum = values.stream().mapToInt(Integer::intValue).sum();
        int min = values.stream().min(Comparator.naturalOrder()).orElse(0);
        int max = values.stream().max(Comparator.naturalOrder()).orElse(0);
        double average = values.stream().mapToInt(Integer::intValue).average().orElse(0);

        return new StatisticsResult(sum, min, max, average);
    }

    private static void sortByYear(List<Star> stars) {
        stars.sort(Comparator.comparingDouble(Star::getAge));
    }

    private static void sortByName(List<Star> stars) {
        stars.sort(Comparator.comparing(Star::getName));
    }

    private static List<Star> parseXml(String xmlFilePath) {
        List<Star> stars = new ArrayList<>();

        try {
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("star");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                Star star = new Star();
                star.setName(getTagValue("name", element));
                star.setConstellation(getTagValue("constellation", element));
                star.setSpectralClass(getTagValue("spectral-class", element));
                star.setRadius(Long.parseLong(getTagValue("radius", element)));
                star.setRotation(getTagValue("rotation", element));
                star.setAge(Double.parseDouble(getTagValue("age", element).split(" ")[0]));
                star.setDistance(getTagValue("distance", element));
                star.setAbsoluteMagnitude(getTagValue("absolute-magnitude", element));

                stars.add(star);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stars;
    }

    private static String getTagValue(String tagName, Element element) {
        NodeList nodeList = element.getElementsByTagName(tagName).item(0).getChildNodes();
        return nodeList.item(0).getNodeValue().trim();
    }

    private static void writeToJson(List<Star> stars, String outputPath) {
        try (FileWriter fileWriter = new FileWriter(outputPath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(stars, fileWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Data
    static class Star {
        private String name;
        private String constellation;
        private String spectralClass;
        private long radius;
        private String rotation;
        private double age;
        private String distance;
        private String absoluteMagnitude;
    }
}