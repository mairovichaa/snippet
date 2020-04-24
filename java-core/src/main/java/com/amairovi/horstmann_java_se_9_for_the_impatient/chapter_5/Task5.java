package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_5;

import java.io.PrintWriter;
import java.util.Scanner;

public class Task5 {


    public static void main(String[] args) {
        Scanner in = null;
        PrintWriter out = null;
        try {
            in = new Scanner("");
            out = new PrintWriter("");
            while (in.hasNext()) {
                out.println(in.next());
            }
        } catch (Exception e) {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception inE) {

                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (Exception outE) {

                }
            }
        }
    }

}
