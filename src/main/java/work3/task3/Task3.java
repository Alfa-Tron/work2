package work3.task3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Task3 {
    public static void main(String[] args) {
        try {
            File folder = new File("src/main/java/work3/task3/zip_var_13");
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
            double sum = 0.0;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;



            jsonNodes.sort(Comparator.comparing(node -> node.get("name").asText()));

            jsonWriter.writeValue(new File("src/main/java/work3/task3/resultName.json"), jsonNodes);
            jsonNodes.sort(Comparator.comparingInt(node -> node.get("radius").asInt()));
            for (JsonNode jsonNode : jsonNodes) {
                JsonNode radiusNode = jsonNode.get("radius");

                double radiusValue = radiusNode.asDouble();
                sum += radiusValue;
                min = Math.min(min, radiusValue);
                max = Math.max(max, radiusValue);

            }

            System.out.println("Статистика для поля 'radius':");
            System.out.println("Сумма: " + sum);
            System.out.println("Минимум: " + min);
            System.out.println("Максимум: " + max);
            System.out.println("Среднее: " + (sum / jsonNodes.size()));

            jsonWriter.writeValue(new File("src/main/java/work3/task3/resultRadius.json"), jsonNodes);


        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    String cleanedValue = fieldValue.textValue().replaceAll("\\s+", " ").trim();
                    objectNode.put(fieldName, cleanedValue);
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