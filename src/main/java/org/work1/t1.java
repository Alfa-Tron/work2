package org.work1;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class t1 {

    public static void main(String[] args) throws IOException {
        String resourcePath = "text_1_var_13";
        File file = new File("src/main/resources/t1");
        ClassLoader classLoader = t1.class.getClassLoader();


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(resourcePath)))) {

            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
            }
            String[] words = text.toString().replaceAll("[^a-zA-Zа-яА-ЯёЁ]", " ").split(" ");
            Map<String, Integer> wordFrequency = new HashMap<>();
            for (String word : words) {
                if (!word.isEmpty()) {
                    wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                }
            }
            Map<String, Integer> sortedWordFrequency = wordFrequency.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Map.Entry<String, Integer> entry : sortedWordFrequency.entrySet()) {
                    writer.write(entry.getKey() + ":" + entry.getValue());
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}