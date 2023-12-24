package com.example.work4.service.task4;

import com.example.work4.model.City;
import com.example.work4.model.Product;
import com.example.work4.repository.T4repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class T4serviceImpl implements T4service {
    private final T4repository repository;

    @Override
    public void readDataAndSaveToDatabase() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Product[] buildingsArray = objectMapper.readValue(new File("src/main/java/com/example/work4/task4RES/task_4_var_13_product_data.json"), Product[].class);
            List<Product> buildings = Arrays.asList(buildingsArray);
            System.out.println(buildings.get(6));
            repository.saveAll(buildings);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void redactData() {
        String filePath1 = "src/main/java/com/example/work4/task4RES/task_4_var_13_update_data.text";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath1));
            String line;
            int k = 0;
            String name = null;
            String method = null;
            String param = null;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("::");
                if (k == 0) {
                    name = parts[1];
                } else if (k == 1) {
                    method = parts[1];
                }
                else if(k==2){
                    if(parts.length==2){
                        param=parts[1];
                        if(method.equals("price_abs")){
                            Product p = repository.findByName(name);
                            p.setPrice(p.getPrice()+Integer.parseInt(param));
                            p.setCount(p.getCount()+1);

                        }
                        if(method.equals("quantity_sub") || method.equals("quantity_add")){
                            Product p = repository.findByName(name);
                            p.setQuantity(p.getQuantity()+Integer.parseInt(param));
                            p.setCount(p.getCount()+1);

                        }
                        if(method.equals("available")){
                            Product p = repository.findByName(name);
                            p.setIsAvailable(Boolean.valueOf(param));
                            p.setCount(p.getCount()+1);

                        }
                        if(method.equals("price_percent")){
                            Product p = repository.findByName(name);
                            p.setPrice(p.getPrice()*(1+Double.parseDouble(param)));
                            p.setCount(p.getCount()+1);

                        }
                    }
                    if(method.equals("remove")){
                        repository.deleteByName(name);
                    }

                    k=-2;
                }

                k++;
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void print1() {
        List<Product> cities = repository.findTop10ByOrderByCountDesc();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        try {

            File file = new File("src/main/java/com/example/work4/task4RES/1.json");
            objectWriter.writeValue(file, cities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void print2() {
        List<String> category = repository.findDistinctCategories();
        for(String c : category){
            List<Product> products = repository.findByCategory(c);
            System.out.println(category +"\\n");
            double sum= 0;
            double min = 112312;
            double max = 0;
            double avg=0;
            for(Product pc : products){
                double ppp = pc.getPrice();
                sum += pc.getPrice();
                if(ppp<min){
                    min = ppp;
                }
                if(ppp>max){
                    max = ppp;
                }

            }
            avg=sum/products.size();
            String filePath = "src/main/java/com/example/work4/task4RES/2";

            // Создаете объект FileWriter для записи в файл
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(new File(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Создаете объект PrintWriter для удобства записи в файл
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Записываете результаты в файл
            printWriter.println("sum " + sum);
            printWriter.println("min " + min);
            printWriter.println("max " + max);
            printWriter.println("avg " +avg);

            // Закрываете ресурсы
            printWriter.close();
            try {
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void print3() {
        Product cities = repository.findById(12L).get();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        try {

            File file = new File("src/main/java/com/example/work4/task4RES/top.json");
            objectWriter.writeValue(file, cities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
