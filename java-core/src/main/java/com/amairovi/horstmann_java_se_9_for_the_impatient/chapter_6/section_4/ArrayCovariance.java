package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.section_4;

import java.util.ArrayList;
import java.util.List;

public class ArrayCovariance {

    private static class Employee {

    }


    private static class Manager extends Employee {

    }

    public static void main(String[] args) {
        Manager[] bosses = new Manager[2];
        Employee[] employees = bosses;
        // throws ArrayStoreException
        employees[0] = new Employee();

        List<Manager> bosses2 = new ArrayList<>();
//        Not possible - avoid problem above
//        List<Employee> employees2 = bosses2;


        List<Employee> employees2 = new ArrayList<>();
        employees2.add(new Employee());
        employees2.add(new Manager());
    }
}
