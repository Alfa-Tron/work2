package work2.task5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class y5 {

    public static void main(String[] args) throws IOException {

        try {
            JsonObject jsonData = new Gson().fromJson(new FileReader("src/main/java/work2/task5/json"), JsonObject.class);
            JsonArray dataArray = jsonData.getAsJsonArray("data");

            Map<String, DescriptiveStatistics> numericFields = new HashMap<>();
            Map<String, Integer> textFieldsFrequency = new HashMap<>();

            for (JsonElement element : dataArray) {
                JsonArray record = element.getAsJsonArray();

                double fieldValue1 = record.get(8).getAsDouble();
                double fieldValue2 = record.get(12).getAsDouble();
                double fieldValue3 = record.get(13).getAsDouble();

                updateStatistics(numericFields, "year", fieldValue1);
                updateStatistics(numericFields, "deaths", fieldValue2);
                updateStatistics(numericFields, "Age-adjusted Death Rate", fieldValue3);

                String textField1 = record.get(11).getAsString();
                String textField2 = record.get(10).getAsString();
                String textField3 = record.get(9).getAsString();

                updateTextFrequency(textFieldsFrequency, textField1);
                updateTextFrequency(textFieldsFrequency, textField2);
                updateTextFrequency(textFieldsFrequency, textField3);
            }

            JsonObject allResults = new JsonObject();

            allResults.add("numericFields", createJsonObjectFromStatistics(numericFields));

            allResults.add("textFieldsFrequency", createJsonObjectFromTextFrequency(textFieldsFrequency));

            saveResultsToJson(allResults, "src/main/java/work2/task5/AllResults.json");
            saveStatisticsToCsv(numericFields,"src/main/java/work2/task5/AllResults.csv");
            saveStatisticsToMessagePack(numericFields,"src/main/java/work2/task5/AllResults.msg");
           saveStatisticsToPickle(numericFields,"src/main/java/work2/task5/AllResults.pkl");
            System.out.println("Расчеты успешно сохранены");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateTextFrequency(Map<String, Integer> textFieldsFrequency, String textField) {
        int frequency = textFieldsFrequency.getOrDefault(textField, 0);
        textFieldsFrequency.put(textField, frequency + 1);
    }

    private static void saveResultsToJson(JsonObject results, String outputPath) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath)) {
            new Gson().toJson(results, writer);
        }
    }

    private static JsonObject createJsonObjectFromStatistics(Map<String, DescriptiveStatistics> numericFields) {
        JsonObject numericResults = new JsonObject();

        for (Map.Entry<String, DescriptiveStatistics> entry : numericFields.entrySet()) {
            String fieldName = entry.getKey();
            DescriptiveStatistics statistics = entry.getValue();

            JsonObject fieldStats = new JsonObject();
            fieldStats.addProperty("max", statistics.getMax());
            fieldStats.addProperty("min", statistics.getMin());
            fieldStats.addProperty("mean", statistics.getMean());
            fieldStats.addProperty("sum", statistics.getSum());
            fieldStats.addProperty("stdDev", statistics.getStandardDeviation());

            numericResults.add(fieldName, fieldStats);
        }

        return numericResults;
    }

    private static JsonObject createJsonObjectFromTextFrequency(Map<String, Integer> textFieldsFrequency) {
        JsonObject textResults = new JsonObject();

        for (Map.Entry<String, Integer> entry : textFieldsFrequency.entrySet()) {
            String fieldName = entry.getKey();
            int frequency = entry.getValue();

            textResults.addProperty(fieldName, frequency);
        }

        return textResults;
    }

    private static void updateStatistics(Map<String, DescriptiveStatistics> numericFields, String fieldName, double value) {
        DescriptiveStatistics statistics = numericFields.getOrDefault(fieldName, new DescriptiveStatistics());
        statistics.addValue(value);
        numericFields.put(fieldName, statistics);
    }
    private static void saveStatisticsToCsv(Map<String, DescriptiveStatistics> numericFields, String outputPath) throws IOException {
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputPath), CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("Field Name", "Max", "Min", "Mean", "Sum", "StdDev");

            for (Map.Entry<String, DescriptiveStatistics> entry : numericFields.entrySet()) {
                String fieldName = entry.getKey();
                DescriptiveStatistics statistics = entry.getValue();

                csvPrinter.printRecord(
                        fieldName,
                        statistics.getMax(),
                        statistics.getMin(),
                        statistics.getMean(),
                        statistics.getSum(),
                        statistics.getStandardDeviation()
                );
            }
        }

    }
    private static void saveStatisticsToMessagePack(Map<String, DescriptiveStatistics> numericFields, String outputPath) throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        packer.packMapHeader(numericFields.size());
        for (Map.Entry<String, DescriptiveStatistics> entry : numericFields.entrySet()) {
            String fieldName = entry.getKey();
            DescriptiveStatistics statistics = entry.getValue();

            packer.packString(fieldName);
            packer.packMapHeader(5);
            packer.packString("Max");
            packer.packDouble(statistics.getMax());
            packer.packString("Min");
            packer.packDouble(statistics.getMin());
            packer.packString("Mean");
            packer.packDouble(statistics.getMean());
            packer.packString("Sum");
            packer.packDouble(statistics.getSum());
            packer.packString("StdDev");
            packer.packDouble(statistics.getStandardDeviation());
        }

        byte[] messagePackData = packer.toByteArray();

        try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath)) {
            fileOutputStream.write(messagePackData);
        }
    }
    private static void saveStatisticsToPickle(Map<String, DescriptiveStatistics> numericFields, String outputPath) throws IOException {// что-то прям в джаве в пикл не знаю как сохранить, специфичные библиотеки только подключать
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            byte[] serializedData = objectMapper.writeValueAsBytes(numericFields);
            objectOutputStream.writeObject(serializedData);
        }
    }
}