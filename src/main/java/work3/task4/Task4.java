package work3.task4;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Task4 {
    public static void main(String[] args) throws IOException {
        File folder = new File("src/main/java/work3/task4/zip_var_13");
        List<JsonNode> jsonNodes = new ArrayList<>();
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectWriter jsonWriter = jsonMapper.writerWithDefaultPrettyPrinter();


        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".xml")) {
                try {
                    ObjectMapper xmlMapper = new XmlMapper();

                    JsonNode jsonNode = xmlMapper.readTree(file);
                    cleanJsonNode(jsonNode);

                    jsonNodes.add(jsonNode);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        jsonNodes.forEach(node -> {
            ArrayNode clothingArray = (ArrayNode) node.get("clothing");
            List<JsonNode> clothingList = new ArrayList<>();
            clothingArray.forEach(clothingList::add);

            clothingList.sort(Comparator.comparingDouble(clothing -> clothing.get("rating").asDouble()));

            ArrayNode sortedClothingArray = jsonMapper.valueToTree(clothingList);
            ((ObjectNode) node).set("clothing", sortedClothingArray);
        });


        jsonWriter.writeValue(new File("src/main/java/work3/task4/ResultRating.json"), jsonNodes);

        jsonNodes.forEach(node -> {
            ArrayNode clothingArray = (ArrayNode) node.get("clothing");
            List<JsonNode> clothingList = new ArrayList<>();
            clothingArray.forEach(clothingList::add);

            clothingList.sort(Comparator.comparing(clothing -> Double.valueOf(clothing.get("price").asText())));

            ArrayNode sortedClothingArray = jsonMapper.valueToTree(clothingList);
            ((ObjectNode) node).set("clothing", sortedClothingArray);
        });

// Сортировка списка по минимальной цене в массиве "clothing"
        jsonNodes.sort(Comparator.comparingDouble(node ->
                node.get("clothing").get(0).get("price").asDouble()));
        double sum = 0.0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (JsonNode jsonNode : jsonNodes) {
            for(int i = 0; i < jsonNode.get("clothing").size();i++){
                JsonNode price = jsonNode.get("clothing").get(i).get("price");
                double price1 = price.asDouble();
                sum += price1;
                min = Math.min(min, price1);
                max = Math.max(max, price1);
            }

        }
        System.out.println("Статистика для поля 'price':");
        System.out.println("Сумма: " + sum);
        System.out.println("Минимум: " + min);
        System.out.println("Максимум: " + max);
        System.out.println("Среднее: " + (sum / jsonNodes.size()));



        jsonWriter.writeValue(new File("src/main/java/work3/task4/ResultPrice.json"), jsonNodes);


    }

    private static void cleanJsonNode(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;

            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> fieldEntry = fields.next();
                String fieldName = fieldEntry.getKey();
                JsonNode fieldValue = fieldEntry.getValue();

                if (fieldValue.isTextual()) {
                    objectNode.put(fieldName, fieldValue.textValue().trim());
                } else if (fieldValue.isObject() || fieldValue.isArray()) {
                    cleanJsonNode(fieldValue);
                }
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;

            for (JsonNode element : arrayNode) {
                cleanJsonNode(element);
            }
        }
    }
}
