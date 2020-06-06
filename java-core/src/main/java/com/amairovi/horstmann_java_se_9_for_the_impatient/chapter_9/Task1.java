package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_9;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;

public class Task1 {

    static void copy1(InputStream in, OutputStream out) {
        try {
            while (true) {
                int data = in.read();
                if (data == -1) {
                    break;
                }
                out.write(data);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final static String PATH_TO_TEMP_FILE = "";

    static void copy2(InputStream in, OutputStream out) {
        try {
            Path path = Paths.get(PATH_TO_TEMP_FILE);
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(path, out);
            out.flush();
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        byte[] initialArray = "Hello world!".getBytes();

        InputStream in = new ByteArrayInputStream(initialArray);
        copy1(in, System.out);

        InputStream in2 = new ByteArrayInputStream(initialArray);
        copy2(in2, System.out);
    }

}
