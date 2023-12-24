package work2.task2;


import org.jetbrains.bio.npy.NpyArray;
import org.jetbrains.bio.npy.NpyFile;
import org.jetbrains.bio.npy.NpzFile;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class y2 {
    public static void main(String[] args) {


        Path filePath = new File("src/main/java/work2/task2/matrix_13_2.npy").toPath();

        NpyArray npyArray = NpyFile.read(filePath, 1000000);
        int[] array = npyArray.asIntArray();
        int rows = npyArray.getShape()[0];
        int cols = npyArray.getShape()[1];

        int[][] twoDimArray = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                twoDimArray[i][j] = array[i * cols + j];
            }
        }
        List<Integer> xList = new ArrayList<>();
        List<Integer> yList = new ArrayList<>();
        List<Integer> zList = new ArrayList<>();

        for (int i = 0; i < twoDimArray.length; i++) {
            for (int j = 0; j < twoDimArray[i].length; j++) {
                if (twoDimArray[i][j] > 513) {
                    xList.add(i);
                    yList.add(j);
                    zList.add(twoDimArray[i][j]);
                }
            }
        }

        int[] x = xList.stream().mapToInt(Integer::intValue).toArray();
        int[] y = yList.stream().mapToInt(Integer::intValue).toArray();
        int[] z = zList.stream().mapToInt(Integer::intValue).toArray();
        Path filePath3 = new File("src/main/java/work2/task2/matrix_13_2NPZ.npz").toPath();
        try(NpzFile.Writer writer = NpzFile.write(filePath3,true)) {
            writer.write("x", x);
            writer.write("y", y);
            writer.write("z", z);
        }


    }
}
