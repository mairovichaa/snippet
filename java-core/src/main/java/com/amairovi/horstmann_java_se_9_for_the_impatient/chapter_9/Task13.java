package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_9;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

public class Task13 {

    private static <T extends Serializable> T clone(T obj) {
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream objOut = new ObjectOutputStream(out)
        ) {

            objOut.writeObject(obj);
            byte[] objectBytes = out.toByteArray();
            try (
                    ByteArrayInputStream in = new ByteArrayInputStream(objectBytes);
                    ObjectInputStream objectIn = new ObjectInputStream(in)
            ) {
                T object = (T) objectIn.readObject();
                return object;

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static class Point implements Serializable {
        private final int x;
        private final int y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }


    public static void main(String[] args) {
        int x = 1;
        int y = 2;
        Point src = new Point(x, y);
        Point clone = clone(src);

        assertThat(clone).isNotSameAs(src);
        assertThat(clone.x).isEqualTo(x);
        assertThat(clone.y).isEqualTo(y);
    }

}
