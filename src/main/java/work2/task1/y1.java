package work2.task1;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.jetbrains.bio.npy.NpyArray;
import org.jetbrains.bio.npy.NpyFile;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class y1 {
    public static void main(String[] args) {


        Path filePath = new File("src/main/java/work2/task1/matrix_13.npy").toPath();

        NpyArray npyArray = NpyFile.read(filePath, 1000);
        int[] array = npyArray.asIntArray();
        int rows = npyArray.getShape()[0];
        int cols = npyArray.getShape()[1];

        double[][] twoDimArray = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                twoDimArray[i][j] = array[i * cols + j];
            }
        }


        RealMatrix matrix = MatrixUtils.createRealMatrix(twoDimArray);
        double sum = 0.0;
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;

        // Найти сумму, максимум и минимум для всех элементов
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                double value = matrix.getEntry(i, j);
                sum += value;
                max = Math.max(max, value);
                min = Math.min(min, value);
            }
        }

        // Найти главную диагональ
        double sumMD = 0.0;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            sumMD += matrix.getEntry(i, i);
        }
        double avrMD = sumMD / matrix.getRowDimension();

        // Найти побочную диагональ
        double sumSD = 0.0;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            sumSD += matrix.getEntry(i, matrix.getColumnDimension() - 1 - i);
        }
        double avrSD = sumSD / matrix.getRowDimension();

        String filePath1 = "src/main/java/work2/task1/1resultJSON.json";



        JSONObject json = new JSONObject();
        json.put("sum", sum);
        json.put("avr", sum / (matrix.getRowDimension() * matrix.getColumnDimension()));
        json.put("sumMD", sumMD);
        json.put("avrMD", avrMD);
        json.put("sumSD", sumSD);
        json.put("avrSD", avrSD);
        json.put("max", max);
        json.put("min", min);
        try (FileWriter fileWriter = new FileWriter(filePath1)) {
            fileWriter.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path filePath3 = new File("src/main/java/work2/task1/matrix_13Normalize.npy").toPath();

        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                double value = matrix.getEntry(i, j);
                double normalizedValue = (value - min) / (max - min);
                matrix.setEntry(i, j, normalizedValue);
            }
        }
        double[] oneDimArray = Arrays.stream(matrix.getData())
                .flatMapToDouble(Arrays::stream)
                .toArray();

        NpyFile.write(filePath3,oneDimArray,new int[]{rows,cols});



    }
}
