package org.work1;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class t4 { public static void main(String[] args) throws IOException, CsvException {
    try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/text_4_var_13"));
         CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/t4"))) {
        List<String[]> lines = reader.readAll();

            for (String[] line : lines) {
            line[5] = null;
        }

        double totalSalary = lines.stream()
                .mapToDouble(line -> Double.parseDouble(line[4].replace("₽", "")))
                .sum();
        double averageSalary = totalSalary / lines.size();

        List<String[]> filteredLines = lines.stream()
                .filter(line -> Double.parseDouble(line[4].replace("₽", "")) >= averageSalary)
                .filter(line -> Integer.parseInt(line[3]) > 28)
                .collect(Collectors.toList());

        filteredLines.sort((line1, line2) -> Integer.compare(Integer.parseInt(line1[0]), Integer.parseInt(line2[0])));

        writer.writeAll(filteredLines);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}