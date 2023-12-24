package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Graf extends JFrame {

    public Graf(String title) {
        super(title);

    ggg("src/2.csv");
    ggg("src/3.csv");
    ggg("src/4.csv");
    ggg("src/5.csv");
    ggg("src/1.csv");

    }

    private void ggg(String csvFile){
        String line;
        String cvsSplitBy = ",";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DefaultPieDataset pieDataset = new DefaultPieDataset();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                for (int i = 0; i < data.length; i++) {
                    String category =""+(i + 1);
                    double value = parseDouble(data[i]);
                    dataset.addValue(value, "Runs", category);
                    pieDataset.setValue(category, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Линейный график
        JFreeChart lineChart = ChartFactory.createLineChart("g1", "x", "y", dataset);
        addChartToTabbedPane(lineChart, "Line Chart");

        // Столбчатый график
        JFreeChart barChart = ChartFactory.createBarChart("g2", "y", "x", dataset);
        addChartToTabbedPane(barChart, "Bar Chart");

        // Круговая диаграмма
        JFreeChart pieChart = ChartFactory.createPieChart("g3", pieDataset, true, true, false);
        addChartToTabbedPane(pieChart, "Pie Chart");

        // Точечный график
        JFreeChart scatterChart = createScatterChart(dataset);
        addChartToTabbedPane(scatterChart, "Scatter Chart");

        // Стековый столбчатый график
        JFreeChart stackedBarChart = createStackedBarChart(dataset);
        addChartToTabbedPane(stackedBarChart, "Stacked Bar Chart");

        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addChartToTabbedPane(JFreeChart chart, String title) {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(title, new ChartPanel(chart));
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
    private JFreeChart createScatterChart(DefaultCategoryDataset dataset) {
        return ChartFactory.createScatterPlot(
                "g4",          // Название графика
                "X Axis",                 // Название оси X
                "Y Axis",                 // Название оси Y
                (XYDataset) dataset                   // Датасет
        );
    }

    private JFreeChart createStackedBarChart(DefaultCategoryDataset dataset) {
        return ChartFactory.createStackedBarChart(
                "g5",      // Название графика
                "x",               // Название оси X
                "y",                  // Название оси Y
                dataset,                  // Датасет
                PlotOrientation.VERTICAL, // Ориентация графика
                true,                     // Легенда
                true,                     // Включить всплывающие подсказки
                false                     // Включить URL-ссылки
        );}

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value.replace("\"", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Graf("Stats Chart"));
    }
}
