package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_9;

public class Task12 {

    public static void main(String[] args) {
        String result = "3:45".replaceAll(
                "(\\d{1,2}):(?<minutes>\\d{2})",
                "$1 hours and ${minutes} minutes"
        );

        System.out.println(result);
        // 3 hours and 45 minutes
    }

}
