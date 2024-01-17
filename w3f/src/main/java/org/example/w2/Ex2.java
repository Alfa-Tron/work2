package org.example.w2;


import com.google.gson.Gson;
import lombok.Data;
import org.example.w1.Book;
import org.example.w1.StatisticsResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ex2 {

    public static void main(String[] args) {
        String folderPath = "zip_var_1322";

        File folder = new File(folderPath);


        File[] listOfFiles = folder.listFiles();
        List<Product> products = new ArrayList<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".html")) {
                    try {
                        Document doc = Jsoup.parse(file, "UTF-8");
                        products.addAll(parseHtml(doc));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        writeToJson(products, "w2All");
        sortByYear(products);
        writeToJson(products,"w2Sort1");
        sortByName(products);
        writeToJson(products,"w2Sort2");
        StatisticsResult statisticsResult = calculateStatistics(products, "bonusPoints");

        Gson gson = new Gson();
        String jsonResult = gson.toJson(statisticsResult);
        try (FileWriter fileWriter = new FileWriter("w2statistics_result.json")) {
            fileWriter.write(jsonResult);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private static void sortByYear(List<Product> products) {
        products.sort(Comparator.comparingInt(Product::getBonusPoints));
    }
    private static void sortByName(List<Product> products) {
        products.sort(Comparator.comparing(Product::getName));
    }
    private static StatisticsResult calculateStatistics(List<Product> products, String numericField) {
        List<Integer> values = new ArrayList<>();
        for (Product product : products) {
            switch (numericField) {
                case "bonusPoints":
                    values.add(product.getBonusPoints());
                    break;
            }
        }

        int sum = values.stream().mapToInt(Integer::intValue).sum();
        int min = values.stream().min(Comparator.naturalOrder()).orElse(0);
        int max = values.stream().max(Comparator.naturalOrder()).orElse(0);
        double average = values.stream().mapToInt(Integer::intValue).average().orElse(0);

        return new StatisticsResult(sum, min, max, average);
    }

    private static List<Product> parseHtml(Document doc) {
        List<Product> products = new ArrayList<>();

        Elements productElements = doc.select(".product-item");
        for (Element productElement : productElements) {
            Product product = new Product();
            product.setName(productElement.select("span").first().text());
            product.setPrice(productElement.select("price").text());
            product.setBonusPoints(parseBonusPoints(productElement.select("strong").text()));
            product.setProcessor(productElement.select("li[type=processor]").text());
            product.setResolution(productElement.select("li[type=resolution]").text());
            product.setCamera(productElement.select("li[type=camera]").text());
            product.setAccumulator(productElement.select("li[type=acc]").text());

            products.add(product);
        }

        return products;
    }
    private static int parseBonusPoints(String bonusText) {
        String[] parts = bonusText.split("\\s");
        if (parts.length >= 2) {
            try {
                return Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static void writeToJson(List<Product> products, String outputPath) {
        try (FileWriter fileWriter = new FileWriter(outputPath)) {
            Gson gson = new Gson();
            gson.toJson(products, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    static class Product {
        private String name;
        private String price;
        private int bonusPoints;
        private String processor;
        private String resolution;
        private String camera;
        private String accumulator;
    }
}