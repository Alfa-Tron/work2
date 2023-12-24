package org.work1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class t5 {  public static void main(String[] args) throws IOException {
    try {
        File htmlFile = new File("src/main/resources/text_5_var_13");

        Document doc = Jsoup.parse(htmlFile, "UTF-8");
        File file = new File("src/main/resources/t5");
        FileWriter csvWriter = new FileWriter(file);

        Element table = doc.select("table").first();

        Elements rows = table.select("tr");

        for (Element row : rows) {
            Elements columns = row.select("td");
            if (columns.size() > 0) {
                String company = columns.get(0).text();
                String contact = columns.get(1).text();
                String country = columns.get(2).text();
                String price = columns.get(3).text();

                csvWriter.append(company).append(",").append(contact).append(",").append(country).append(",").append(price).append("\n");
            }
        }

        csvWriter.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}