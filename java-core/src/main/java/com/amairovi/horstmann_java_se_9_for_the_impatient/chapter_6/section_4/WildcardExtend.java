package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.section_4;

import java.util.ArrayList;
import java.util.List;

public class WildcardExtend {


    private static class Employee {

    }


    private static class Manager extends Employee {

    }

    public static void main(String[] args) {

        List<? extends Employee> employees = new ArrayList<>();

        // not allowed as '? extends Employee' literally means any subtype of Employee,
        // for example, List<Employee>, List<Manager>, etc
        // so there is no value one can add to it except null
//        employees.add(new Employee());
//        employees.add(new Manager());
        employees.add(null);

    }

}
