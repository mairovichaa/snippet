package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_4.section_4_4_5;

import java.util.Random;

public class RandomCounterInterfaceImpl implements CounterInterface {

    private final Random random = new Random();

    @Override
    public int count() {
        return random.nextInt(1000);
    }

}
