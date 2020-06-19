package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_9;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Task6 {


    private final static int LONG_SIZE_IN_BYTES = 8;

    //    I was too lazy to deal with bmp format, so I decided to work with file containing matrix instead
    private static void replaceMember(int maxAmountOfCols, int row, int col, long value, RandomAccessFile file) throws IOException {

        int rowSize = maxAmountOfCols * LONG_SIZE_IN_BYTES;

        int pos = row * rowSize + col * LONG_SIZE_IN_BYTES;

        file.seek(pos);
        file.writeLong(value);
    }

    private static void createFile(int rowNum, int colNum, RandomAccessFile rw) throws IOException {
        for (int i = 1; i <= rowNum * colNum; i++) {
            rw.writeLong(i);
        }
    }

    private static void printFile(int rowNum, int colNum, RandomAccessFile rw) throws IOException {
        rw.seek(0);
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                System.out.print(rw.readLong() + " ");
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        int amountOfRows = 4;
        int amountOfCols = 5;
        Path path = Paths.get("path_to_place_where_matrix_should_be_created");

        try (RandomAccessFile rw = new RandomAccessFile(path.toFile(), "rw")) {
            createFile(amountOfRows, amountOfCols, rw);
            printFile(amountOfRows, amountOfCols, rw);
            replaceMember(amountOfCols, 1, 1, 10, rw);
            System.out.println();
            printFile(amountOfRows, amountOfCols, rw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
