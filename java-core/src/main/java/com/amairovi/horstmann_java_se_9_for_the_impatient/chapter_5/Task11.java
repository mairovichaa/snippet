package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_5;

public class Task11 {

    public static void main(String[] args) {
        new Task11().factorial(5);
    }


    public long factorial(int op) {
        long result = op == 1 ? 1 : op * factorial(op - 1);

        new Exception().printStackTrace();

        return result;
    }

}
