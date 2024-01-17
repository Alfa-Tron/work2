package org.example.w1;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ex1 {

    public static void main(String[] args) throws IOException {
        File folder = new File("zip_var_13");
        File[] listOfFiles = folder.listFiles();
        List<Book> books = new ArrayList<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".html")) {
                    try {
                        Document doc = Jsoup.parse(file, "UTF-8");
                        books.add(parseHtml(doc).get(0));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        writeToJson(books, "w1All");
        sortByYear(books);
        writeToJson(books, "w1Sort1");
        sortByPage(books);
        writeToJson(books,"w1Sort2");
        StatisticsResult statisticsResult = calculateStatistics(books, "pages");

        Gson gson = new Gson();
        String jsonResult = gson.toJson(statisticsResult);
        try (FileWriter fileWriter = new FileWriter("statistics_result.json")) {
            fileWriter.write(jsonResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Book> parseHtml(Document doc) {
        List<Book> books = new ArrayList<>();

        Elements bookElements = doc.select(".book-wrapper");
        for (Element bookElement : bookElements) {
            Book book = new Book();
            book.setCategory(bookElement.select("span:contains(Категория)").text().replace("Категория: ", ""));
            book.setTitle(bookElement.select(".book-title").text());
            book.setAuthor(bookElement.select(".author-p").text());
            book.setPages(Integer.parseInt(bookElement.select("span:contains(Объем)").text().replaceAll("\\D+", "")));
            book.setYear(Integer.parseInt(bookElement.select("span:contains(Издано)").text().replaceAll("\\D+", "")));
            book.setIsbn(bookElement.select("span:contains(ISBN)").text().replace("ISBN:", "").trim());
            book.setDescription(bookElement.select("p:contains(Описание)").text());
            book.setRating(Double.parseDouble(bookElement.select("span:contains(Рейтинг)").text().replaceAll("\\D+", "")));
            book.setViews(Integer.parseInt(bookElement.select("span:contains(Просмотры)").text().replaceAll("\\D+", "")));

            books.add(book);
        }

        return books;
    }

    private static void writeToJson(List<Book> books, String outputPath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(outputPath)) {
            Gson gson = new Gson();
            gson.toJson(books, fileWriter);
        }
    }

    private static void sortByYear(List<Book> books) {
        books.sort(Comparator.comparingInt(Book::getYear));
    }
    private static void sortByPage(List<Book> books) {
        books.sort(Comparator.comparingInt(Book::getPages));
    }

    private static StatisticsResult calculateStatistics(List<Book> books, String numericField) {
        List<Integer> values = new ArrayList<>();
        for (Book book : books) {
            switch (numericField) {
                case "pages":
                    values.add(book.getPages());
                    break;
            }
        }

        int sum = values.stream().mapToInt(Integer::intValue).sum();
        int min = values.stream().min(Comparator.naturalOrder()).orElse(0);
        int max = values.stream().max(Comparator.naturalOrder()).orElse(0);
        double average = values.stream().mapToInt(Integer::intValue).average().orElse(0);

        return new StatisticsResult(sum, min, max, average);
    }
}
