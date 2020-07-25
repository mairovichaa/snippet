package com.amairovi.goetz_concurrency_in_practice.chapter_5;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HiddenIterator {

    private final Set<Integer> set = new HashSet<>();

    public synchronized void add(Integer i) {
        set.add(i);
    }


    public synchronized void remove(Integer i){
        set.remove(i);
    }

    public void addTenThings() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            add(r.nextInt());
        }
        // iteration hidden within string concatenation
        System.out.println("DEBUG: added ten elements to " + set);
    }
}
