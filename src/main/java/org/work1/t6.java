package org.work1;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class t6 {
    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("https://evilinsult.com/generate_insult.php?lang=en&type=json");
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                String json = EntityUtils.toString(entity);

                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                String html = createHtmlFromJsonObject(jsonObject);

                System.out.println(html);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String createHtmlFromJsonObject(JsonObject jsonObject) {
        String html = "<html><body>";
        for (String key : jsonObject.keySet()) {
            String value = jsonObject.get(key).getAsString();
            html += "<p><strong>" + key + ":</strong> " + value + "</p>";
        }
        html += "</body></html>";
        return html;
    }
}
