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

public class Task15 {

    @ToString
//     new version
    private static class Point implements Serializable {

        private static final long serialVersionUID = 1L;

        private int[] coors;

        private Point(int x, int y) {
            coors = new int[]{x, y};
        }

        private final static int[] DEFAULT_COORS = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE};

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            ObjectInputStream.GetField getField = in.readFields();

            ObjectStreamClass clazz = getField.getObjectStreamClass();
            if (clazz.getField("coors") != null) {
//               new format
                coors = (int[]) getField.get("coors", DEFAULT_COORS);
            } else {
//               old format
                int x = getField.get("x", Integer.MIN_VALUE);
                int y = getField.get("y", Integer.MIN_VALUE);
                coors = new int[]{x, y};
            }
        }

    }


//    @ToString
//    // old version
//    private static class Point implements Serializable {
//
////      if serialVersionUID isn't present, then it will be generated automatically
////      in such case it's likely that its value will differ
////      different serialVersionUID's values lead to exception during deserialization
//        private static final long serialVersionUID = 1L;
//
//        private final int x;
//        private final int y;
//
//        private Point(int x, int y) {
//            this.x = x;
//            this.y = y;
//        }
//
//    }


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

    private static String PATH_TO_FILE_WITH_OLD = "path_to_old";

    private static String PATH_TO_FILE_WITH_NEW = "path_to_new";

    public static void main(String[] args) {
        Path path = Paths.get(PATH_TO_FILE_WITH_NEW);

        List<Point> points = IntStream.range(1, 3)
                .mapToObj(num -> new Point(num, num * 2))
                .collect(Collectors.toList());
        write(points, path);

        List<Point> oldPoints = read(Paths.get(PATH_TO_FILE_WITH_OLD));
        System.out.println(oldPoints);


        List<Point> newPoints = read(Paths.get(PATH_TO_FILE_WITH_NEW));
        System.out.println(newPoints);
    }

}
