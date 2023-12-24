package org.example;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import com.opencsv.CSVReader;
import org.jsoup.Jsoup;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class CSVPlotter {
    public static void main(String[] args) throws IOException {
        String[] csvFiles = {"src/[1]game_logs.csv", "src/[3]flights.csv", "src/[4]vacancies.csv.gz", "src/5dataset.csv"};

        for (String csvFile : csvFiles) {
            processCSV(csvFile);
        }

      /* String inputFilePath = "src/[1]game_logs.csv";
    String outputFilePath = "src/1.csv";

    try (CSVReader reader = new CSVReader(new FileReader(inputFilePath));
         CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath))) {

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            String[] selectedColumns = new String[11];
            System.arraycopy(nextLine, 25, selectedColumns, 0, 10);

            writer.writeNext(selectedColumns);
        }
    } catch (CsvValidationException e) {
        throw new RuntimeException(e);
    }
        String inputFilePath2 = "src/2_Automotive_Kaggle_Sample.csv";
        String outputFilePath2 = "src/2.csv";

     try (CSVReader reader = new CSVReader(new FileReader(inputFilePath2));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath2))) {

            String[] nextLine;
             while ((nextLine = reader.readNext()) != null) {
                String[] selectedColumns = selectColumns(nextLine,  4, 5, 12);
                writer.writeNext(selectedColumns);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }



        String inputFilePath3 = "src/[3]flights.csv";
        String outputFilePath3 = "src/3.csv";

        try (CSVReader reader = new CSVReader(new FileReader(inputFilePath3));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath3))) {

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String[] selectedColumns = selectColumns(nextLine,  0,1,2,3,5,11,12,15,16,17);
                writer.writeNext(selectedColumns);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        String inputFilePath4 = "src/[4]vacancies.csv.gz";
        String outputFilePath4 = "src/4.csv";

        try (CSVReader reader = new CSVReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(inputFilePath4))));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath4))) {

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String[] selectedColumns = selectColumns(nextLine,  0,36,45,46);
                cleanHtmlTags(selectedColumns);
                writer.writeNext(selectedColumns);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        String inputFilePath5 = "src/5dataset.csv";
        String outputFilePath5 = "src/5.csv";

        try (CSVReader reader = new CSVReader(new FileReader(inputFilePath5));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath5))) {

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String[] selectedColumns = selectColumns(nextLine,  10,11,13,14,15,16,17,18,19);
                writer.writeNext(selectedColumns);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }/**/


    }
    private static void cleanHtmlTags(String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i] != null) {
                columns[i] = Jsoup.parse(columns[i]).text();
            }
        }
    }
    private static String[] selectColumns(String[] source, int... columnIndices) {
        String[] selectedColumns = new String[columnIndices.length];
        for (int i = 0; i < columnIndices.length; i++) {
            int columnIndex = columnIndices[i];
            if (columnIndex >= 0 && columnIndex < source.length) {
                selectedColumns[i] = source[columnIndex];
            } else {
                selectedColumns[i] = "";
            }
        }
        return selectedColumns;}
    private static void processCSV(String csvFile) {
        try {
            // Объем памяти на диске
            long diskSpace = getFileSize(csvFile);
            System.out.println("Disk space for " + csvFile + ": " + diskSpace + " bytes");

            // Создаем JSON с результатами анализа
            Map<String, Long> result = new HashMap<>();
            result.put("diskSpace", diskSpace);

            try (FileWriter writer = new FileWriter(csvFile+"1")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String jsonResult = gson.toJson(result);
                writer.write(jsonResult);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long getFileSize(String filePath) throws IOException {
        return java.nio.file.Files.size(java.nio.file.Paths.get(filePath));
    }


}



