package org.work1;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;


public class t2 {

    public static void main(String[] args) {
        String resourcePath = "text_2_var_13";
        File file = new File("src/main/resources/t2");
        ClassLoader classLoader = t1.class.getClassLoader();


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(resourcePath)))) {

            String line;
            ArrayList<String> res = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] f = line.split(":");
                BigDecimal bigDecimal = new BigDecimal(0);
                for(String n : f){
                 bigDecimal=  bigDecimal.add(new BigDecimal(n));
                }
                res.add(bigDecimal.divide(new BigDecimal(1)).toString());


            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for(String s : res){
                    writer.write(s);
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