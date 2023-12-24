package work3.task1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public class Task1 {
    public static void main(String[] args) {
        List<JsonObject> data = new ArrayList<>();

        File folder = new File("src/main/java/work3/task1/zip_var_13");

        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".html")) {
                try {
                    Document doc = Jsoup.parse(file, "UTF-8");
                    JsonObject buildingInfo = getBuildingInfo(doc.html());
                    data.add(buildingInfo);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // сортировка по этажам.
        List<JsonObject> sortedDataFloor = data.stream()
                .sorted(Comparator.comparing(json -> json.getAsJsonObject("building")
                        .getAsJsonObject("buildingDetails").get("floors").getAsString()))
                .collect(Collectors.toList());
        // сортировка по индексу
        List<JsonObject> sortedDataIndex = data.stream()
                .sorted(Comparator.comparing(json -> json.getAsJsonObject("building")
                        .getAsJsonObject("address").get("index").getAsString()))
                .collect(Collectors.toList());
        // стат характеристики
        List<Integer> floorValues = data.stream().map(json -> json.getAsJsonObject("building")
                .getAsJsonObject("buildingDetails").get("floors")
                .getAsInt()).collect(Collectors.toList());

        int sum = floorValues.stream().mapToInt(Integer::intValue).sum();
        int min = floorValues.stream().mapToInt(Integer::intValue).min().orElse(0);
        int max = floorValues.stream().mapToInt(Integer::intValue).max().orElse(0);
        double average = floorValues.stream().mapToInt(Integer::intValue).average().orElse(0.0);

        System.out.println("Сумма этажей: " + sum);
        System.out.println("Минимальное количество этажей: " + min);
        System.out.println("Максимальное количество этажей: " + max);
        System.out.println("Среднее количество этажей: " + average);

        String filePath1 = "src/main/java/work3/task1/result.json";
        String filePath2 = "src/main/java/work3/task1/resultIndex.json";
        String filePath3 = "src/main/java/work3/task1/resultFloor.json";


        try {
            Files.write(Path.of(filePath1), gson.toJson(data).getBytes(), StandardOpenOption.CREATE);
            Files.write(Path.of(filePath2), gson.toJson(sortedDataIndex).getBytes(), StandardOpenOption.CREATE);
            Files.write(Path.of(filePath3), gson.toJson(sortedDataFloor).getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonObject getBuildingInfo(String html) {
        JsonObject jsonObject = new JsonObject();
        Document document = Jsoup.parse(html);

        Elements buildDivs = document.select(".build-wrapper");
        if (!buildDivs.isEmpty()) {
            Element buildDiv = buildDivs.first();

            String city = buildDiv.selectFirst("span:contains(Город)").ownText().split(" ", 2)[1];
            jsonObject.addProperty("city", city);

            Element buildingInfo = buildDiv.selectFirst(".title");
            if (buildingInfo != null) {
                JsonObject buildingObject = new JsonObject();
                buildingObject.addProperty("name", buildingInfo.text().split(" ", 2)[1]);

                Element addressInfo = buildDiv.selectFirst(".address-p");
                if (addressInfo != null) {
                    String[] addressParts = addressInfo.text().split("Индекс:");
                    JsonObject addressObject = new JsonObject();
                    addressObject.addProperty("street", addressParts[0].trim().split(" ", 2)[1]);
                    addressObject.addProperty("index", addressParts[1].trim());
                    buildingObject.add("address", addressObject);
                }

                Element detailsInfo = buildDiv.selectFirst("div:contains(Информация о строении)");
                if (detailsInfo != null) {
                    JsonObject detailsObject = new JsonObject();
                    detailsObject.addProperty("floors", detailsInfo.select(".floors").text().split(" ", 2)[1]);
                    detailsObject.addProperty("year", detailsInfo.select(".year").text());
                    buildingObject.add("buildingDetails", detailsObject);
                }

                jsonObject.add("building", buildingObject);
            }

            Element ratingAndViewsElement = buildDiv.selectFirst("span:contains(Рейтинг)");
            if (ratingAndViewsElement != null) {
                String ratingAndViewsText = ratingAndViewsElement.ownText().replace("Рейтинг:", "").trim();
                String[] ratingAndViews = ratingAndViewsText.split("Просмотры:");
                if (ratingAndViews.length > 1) {
                    String ratingText = ratingAndViews[0].trim();
                    jsonObject.addProperty("rating", Double.parseDouble(ratingText));

                    String viewsText = ratingAndViews[1].trim();
                    jsonObject.addProperty("views", Integer.parseInt(viewsText));
                }

                String parkingText = buildDiv.select("span:contains(Парковка)").text().replace("Парковка:", "").trim();
                jsonObject.addProperty("parking", parkingText);
            }
        }

        return jsonObject;
    }
}