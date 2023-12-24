package org.work1;

import java.io.*;

public class t3 {

    public static void main(String[] args) {
        String resourcePath = "text_3_var_13";
        File file = new File("src/main/resources/t3");
        ClassLoader classLoader = t1.class.getClassLoader();


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(resourcePath)))) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] f = line.split(",");
                    for (int i = 0; i < f.length - 1; i++) {
                        if (f[i].equals("NA")) {
                            f[i] = String.valueOf(((Double.parseDouble(f[i - 1]) + Double.parseDouble(f[i + 1])) / 2));
                        }
                    }
                    for (String s : f) {
                        double d = Double.parseDouble(s);
                        if (63 < Math.sqrt(d))
                            writer.write(s + ",");
                    }
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