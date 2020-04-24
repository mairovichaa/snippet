package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_4.section_4_4_5;

public class SimpleCounterInterfaceImpl implements CounterInterface {

    private int count = 0;
    @Override
    public int count() {
        return count++;
    }

}
