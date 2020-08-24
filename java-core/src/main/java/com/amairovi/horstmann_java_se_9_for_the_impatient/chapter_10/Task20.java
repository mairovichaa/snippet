package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

public class Task20 {

    public static class Stack {
        class Node {
            Object value;
            Node next;
        }

        private Node top;

        public void push(Object newValue) {
            Node n = new Node();
            n.value = newValue;
            n.next = top;
            top = n;
        }

        public Object pop() {
            if (top == null) {
                return null;
            }
            Node n = top;
            top = n.next;
            return n.value;
        }
    }
}
