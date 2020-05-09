package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

public class Task8 {

    private static class Pair<E extends Comparable<? super E>> {

        private final E first;
        private final E second;

        private Pair(E first, E second) {
            this.first = first;
            this.second = second;
        }


        public E getFirst() {
            return first;
        }

        public E getSecond() {
            return second;
        }

        public E min() {
            return max() == first ? second : first;
        }

        public E max() {
            return first.compareTo(second) >= 0 ? first : second;
        }

    }


    static class Parent implements Comparable<Parent> {

        @Override
        public int compareTo(Parent o) {
            return 0;
        }

    }

    static class Child extends Parent {

    }

    public static void main(String[] args) {
        Child child = new Child();

//      Comparable<? super E> allows this assignment as Child is Comparable<Parent> (Comparable<? super Child>)
//      Comparable<E> won't allow it. In such case only Pair<Parent> would be allowed type
        Pair<Child> parentPair = new Pair<>(child, child);
    }

}
