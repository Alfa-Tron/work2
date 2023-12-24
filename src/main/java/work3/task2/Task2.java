package work3.task2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Task2 {
     public static void main(String[] args) {
        List<JsonObject> data = new ArrayList<>();

        File folder = new File("src/main/java/work3/task2/zip_var_13");

        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".html")) {
                try {
                    Document doc = Jsoup.parse(file, "UTF-8");
                    List<JsonObject> buildingInfo = parseProducts(doc.html());

                    data.addAll(buildingInfo);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
         List<JsonObject> sortedDataPrice = data.stream()
                 .sorted(Comparator.comparing(json -> {
                     String priceString = json.getAsJsonPrimitive("price").getAsString();
                     String numericPart = priceString.replaceAll("[^\\d.]", ""); // Keep only digits and dots
                     return Double.parseDouble(numericPart);
                 }))
                 .collect(Collectors.toList());

         List<JsonObject> sortedDataBonus = data.stream()
                 .sorted(Comparator.comparing(json -> json.get("bonusInfo").getAsString()))
                 .collect(Collectors.toList());
         List<Integer> floorValues = data.stream()
                 .map(json -> {
                     String priceString = json.getAsJsonPrimitive("price").getAsString();
                     String numericPart = priceString.replaceAll("[^\\d.]", ""); // Keep only digits and dots
                     return Integer.parseInt(numericPart);
                 })
                 .collect(Collectors.toList());
         int sum = floorValues.stream().mapToInt(Integer::intValue).sum();
         int min = floorValues.stream().mapToInt(Integer::intValue).min().orElse(0);
         int max = floorValues.stream().mapToInt(Integer::intValue).max().orElse(0);
         double average = floorValues.stream().mapToInt(Integer::intValue).average().orElse(0.0);
         System.out.println("Сумма стоимости: " + sum);
         System.out.println("Минимальная стоимость: " + min);
         System.out.println("Максимальноая стоимость: " + max);
         System.out.println("Средняя стоимость: " + average);

         String filePath1 = "src/main/java/work3/task2/result.json";
         String filePath2 = "src/main/java/work3/task2/resultPrice.json";
         String filePath3 = "src/main/java/work3/task2/resultBonus.json";

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
         try {
             Files.write(Path.of(filePath1), gson.toJson(data).getBytes(), StandardOpenOption.CREATE);
             Files.write(Path.of(filePath2), gson.toJson(sortedDataPrice).getBytes(), StandardOpenOption.CREATE);
             Files.write(Path.of(filePath3), gson.toJson(sortedDataBonus).getBytes(), StandardOpenOption.CREATE);
         } catch (IOException e) {
             e.printStackTrace();
         }

     }
     private static List<JsonObject> parseProducts(String html) {
        List<JsonObject> products = new ArrayList<>();
        Document document = Jsoup.parse(html);

        Elements productElements = document.select(".product-item");
        for (Element productElement : productElements) {
            JsonObject productObject = new JsonObject();

            String title = productElement.select("span").first().text();
            String price = productElement.select("price").first().text();
            String bonusInfo = productElement.select("strong").first().text().split(" ")[2];

            JsonArray characteristics = new JsonArray();
            Elements characteristicElements = productElement.select("li");
            for (Element characteristicElement : characteristicElements) {
                JsonObject characteristicObject = new JsonObject();
                characteristicObject.addProperty("type", characteristicElement.attr("type"));
                characteristicObject.addProperty("value", characteristicElement.text());
                characteristics.add(characteristicObject);
            }

            productObject.addProperty("title", title);
            productObject.addProperty("price", price);
            productObject.addProperty("bonusInfo", bonusInfo);
            productObject.add("characteristics", characteristics);

            products.add(productObject);
        }

        return products;
    }
}