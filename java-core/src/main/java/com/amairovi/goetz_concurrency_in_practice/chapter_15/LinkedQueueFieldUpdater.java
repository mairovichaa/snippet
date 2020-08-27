package com.amairovi.goetz_concurrency_in_practice.chapter_15;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class LinkedQueueFieldUpdater<E> {
    private static class Node<E> {
        private final E item;
        private volatile Node<E> next;

        public Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }

        private static AtomicReferenceFieldUpdater<Node, Node> nextUpdater =
                AtomicReferenceFieldUpdater.newUpdater(Node.class, Node.class, "next");

    }


    private final Node<E> dummy = new Node<>(null, null);
    private final AtomicReference<Node<E>> head = new AtomicReference<>(dummy);
    private final AtomicReference<Node<E>> tail = new AtomicReference<>(dummy);

    public boolean put(E item) {
        Node<E> newNode = new Node<>(item, null);
        while (true) {
            Node<E> curTail = tail.get();
            Node<E> tailNext = curTail.next;
            if (tailNext != null) {
                // Queue in intermediate state, advance tail
                tail.compareAndSet(curTail, tailNext);
            } else {
                // In quiescent state, try inserting new node
                if (Node.nextUpdater.compareAndSet(curTail, null, newNode)) {
                    // Insertion succeeded, try advancing tail
                    tail.compareAndSet(curTail, newNode);
                    return true;
                }
            }
        }
    }

    public void print() {
        Node<E> node = dummy.next;
        if (node == null) {
            System.out.println("Queue is empty!");
            return;
        }

        while (node != null) {
            System.out.print(node.item);
            node = node.next;
            if (node != null) {
                System.out.print(" -> ");
            }
        }
    }

    public static void main(String[] args) {
        LinkedQueueFieldUpdater<String> queue = new LinkedQueueFieldUpdater<>();

        for (int i = 0; i < 10; i++) {
            queue.put("str" + i);
        }

        queue.print();
    }
}
