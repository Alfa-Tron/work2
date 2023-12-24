package work2.task4;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import work2.task3.y3;

import java.io.*;
import java.util.*;

public class y4 {
    public static void main(String[] args) throws IOException {

        //  FileInputStream fileInputStream = new FileInputStream("src/main/java/work2/task4/products_13.json");
        Map<String, Product> productMap = new HashMap<>();

        try (Reader reader = new FileReader("src/main/java/work2/task4/products_13.json")) {
            Gson gson = new Gson();

            // Используйте TypeToken для сохранения типа List<Product>
            TypeToken<List<Product>> token = new TypeToken<>() {
            };
            List<Product> products = gson.fromJson(reader, token.getType());

            // Заполняем Map данными из списка
            for (Product product : products) {
                // Предположим, что у класса Product есть метод getName(), возвращающий название товара
                String productName = product.getName();
                productMap.put(productName, product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, ProductInfo> productMapInfo = new HashMap<>();

        try (Reader reader = new FileReader("src/main/java/work2/task4/price_info_13.json")) {
            Gson gson = new Gson();

            // Используйте TypeToken для сохранения типа List<Product>
            TypeToken<List<ProductInfo>> token = new TypeToken<>() {
            };
            List<ProductInfo> productsInfo = gson.fromJson(reader, token.getType());

            // Заполняем Map данными из списка
            for (ProductInfo productInfo : productsInfo) {
                // Предположим, что у класса Product есть метод getName(), возвращающий название товара
                String productName = productInfo.getName();
                productMapInfo.put(productName, productInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        ObjectMapper objectMapper = new ObjectMapper();

        List<Product> productSummaries = new ArrayList<>();

        for (Map.Entry<String, ProductInfo> entry : productMapInfo.entrySet()) {
            String key = entry.getKey();
            ProductInfo f = entry.getValue();
            String method = f.getMethod();
            Product p = productMap.get(key);
            switch (method) {
                case ("add"): {
                    Double price = productMap.get(key).getPrice();
                    price += f.getParam();
                    p.setPrice(price);
                    break;
                }
                case ("sub"):{
                    Double price = productMap.get(key).getPrice();
                    price -= f.getParam();
                    p.setPrice(price);

                    break;
                }
                case ("percent+"):{
                    Double price = productMap.get(key).getPrice();
                    price = price*(1+f.getParam());
                    p.setPrice(price);

                    break;
                }
                case ("percent-"):{
                    Double price = productMap.get(key).getPrice();
                    price = price*(1-f.getParam());
                    p.setPrice(price);

                    break;
                }

            }
            productSummaries.add(productMap.get(key));
        }

        String filePath = "src/main/java/work2/task4/3resultJSON.json";
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, productSummaries);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @JsonInclude
    static class Product {
        @JsonProperty("name")
        private String name;
        @JsonProperty("price")
        private Double price;

        public Product(String name, Double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public Double getPrice() {
            return price;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }

    static class ProductInfo {
        private String name;
        private String method;
        private double param;

        public ProductInfo(String name, String method, double param) {
            this.name = name;
            this.method = method;
            this.param = param;
        }

        public String getName() {
            return name;
        }

        public String getMethod() {
            return method;
        }

        public double getParam() {
            return param;
        }
    }


}