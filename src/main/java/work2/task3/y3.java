package work2.task3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


import java.io.*;
import java.util.*;

public class y3 {
    public static void main(String[] args) throws IOException {
        List<Product> products = new ArrayList<>();

        try (Reader reader = new FileReader("src/main/java/work2/task3/products_13.json")) {
            Gson gson = new Gson();
            Product[] productArray = gson.fromJson(reader, Product[].class);
            products.addAll(Arrays.asList(productArray));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, List<Double>> productInfo = new HashMap<>();
        for (Product product : products) {
            productInfo.computeIfAbsent(product.getName(), k -> new ArrayList<>()).add(product.getPrice());
        }

        ObjectMapper objectMapper = new ObjectMapper();

        List<ProductSummary> productSummaries = new ArrayList<>();

        for (Map.Entry<String, List<Double>> entry : productInfo.entrySet()) {
            String key = entry.getKey();
            List<Double> prices = entry.getValue();
            double max = Collections.max(prices);
            double min = Collections.min(prices);
            double avg = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

            ProductSummary productSummary = new ProductSummary(key, max, min, avg);

            productSummaries.add(productSummary);
        }

        String filePath = "src/main/java/work2/task3/3resultJSON.json";
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, productSummaries);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class Product {
        private String name;
        private double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }
    @JsonInclude
    static class ProductSummary {
        private String name;
        private double maxPrice;
        private double minPrice;
        private double avgPrice;

        public ProductSummary(String name, double maxPrice, double minPrice, double avgPrice) {
            this.name = name;
            this.maxPrice = maxPrice;
            this.minPrice = minPrice;
            this.avgPrice = avgPrice;

        }
        @JsonProperty("name")
        public String getName() {
            return name;
        }
        @JsonProperty("maxPrice")
        public double getMaxPrice() {
            return maxPrice;
        }
        @JsonProperty("minPrice")
        public double getMinPrice() {
            return minPrice;
        }
        @JsonProperty("avgPrice")
        public double getAvgPrice() {
            return avgPrice;
        }
    }


}