package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.section_6;

public class MethodClash {
    public static class Employee implements Comparable<Employee>{

        @Override
        public int compareTo(Employee o) {
            return 0;
        }

    }

    // it's not allowed as there will be a bridge method that will make type conversion
    // but it seems that it's not clever enough two add instance of operator to
    // make right conversion
//    public static class Manager extends Employee implements Comparable<Manager>{
//
//    }
//
//    bridge
//    public int compareTo(Object o) {
//        return compareTo((Employee) o);
//    }
//    methods of Manager
//    public int compareTo(Employee);
//    public int compareTo(Manager);

}
