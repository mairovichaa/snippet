package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class Task1 {

    public static Set<Integer> m(int n) {
        HashSet<Integer> numbers = new HashSet<>();
        for (int i = 2; i <= n; i++) {
            numbers.add(i);
        }

        for (int s = 2; s * s <= n; s++) {
            for (int cur = s; s * cur <= n; cur++) {
                int notPrime = cur * s;
                numbers.remove(notPrime);
            }
        }

        return numbers;
    }

    public static BitSet m2(int n) {
        BitSet numbers = new BitSet();
        for (int i = 2; i <= n; i++) {
            numbers.set(i);
        }

        for (int s = 2; s * s <= n; s++) {
            for (int cur = s; s * cur <= n; cur++) {
                int notPrime = cur * s;
                numbers.clear(notPrime);
            }
        }

        return numbers;
    }

    public static void main(String[] args) {
        Set<Integer> m = m(100);
        System.out.println(m);
        BitSet m2 = m2(100);
        System.out.println(m2);
    }

}
