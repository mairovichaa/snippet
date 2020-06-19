package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_9;

import lombok.ToString;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Task14 {

    @ToString
    private static class Point implements Serializable {

        private final int x;
        private final int y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }


    private static void write(List<Point> points, Path path) {
        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE);
             ObjectOutputStream objOut = new ObjectOutputStream(out)) {
            objOut.writeObject(points);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Point> read(Path path) {
        try (InputStream in = Files.newInputStream(path);
             ObjectInputStream objIn = new ObjectInputStream(in)) {
            return (List<Point>) objIn.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        List<Point> points = IntStream.range(0, 10)
                .mapToObj(num -> new Point(num, num * 2))
                .collect(Collectors.toList());

        String PATH_TO_FILE = "{replace_with_real_path}";
        Path path = Paths.get(PATH_TO_FILE);
        write(points, path);

        List<Point> savedPoints = read(path);
        System.out.println(savedPoints);
    }

}
