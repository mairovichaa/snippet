package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Task22 {

    private static class Stack {
        Object myLock = "LOCK";
        Object myLock2 = new Object();

        private void push() {
            // due to the nature of the String could be shared between several instances of Stack
            synchronized (myLock) {
            }
        }
    }

    public static void main(String[] args) {
        Stack stack1 = new Stack();
        System.out.println(stack1);
        Stack stack2 = new Stack();
        System.out.println(stack2);

        // for String fields the same object is used
        assertThat(stack1.myLock).isSameAs(stack2.myLock);

        // for Object fields different objects are used
        assertThat(stack1.myLock2).isNotSameAs(stack2.myLock2);
    }

}
