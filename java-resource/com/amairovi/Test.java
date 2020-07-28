package com.amairovi;

import java.util.Scanner;

public class Test {

       public static void main(String[] args) {
       	String pathToFile = args[0];
        try (Scanner sc = new Scanner(Test.class.getResourceAsStream(pathToFile))) {
            while (sc.hasNext()){
                String line = sc.nextLine();
                System.out.println(line);
            }
        }
    }
}
