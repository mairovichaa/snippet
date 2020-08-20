package com.amairovi.goetz_concurrency_in_practice.chapter_14;

import com.amairovi.goetz_concurrency_in_practice.ThreadSafe;

@ThreadSafe
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V> {
    public GrumpyBoundedBuffer(int size) {
        super(size);
    }

    public synchronized void put(V v) throws BufferFullException {
        if (isFull()) {
            throw new BufferFullException();
        }
        doPut(v);
    }

    public synchronized V take() throws BufferEmptyException {
        if (isEmpty()) {
            throw new BufferEmptyException();
        }
        return doTake();
    }

    public static class BufferFullException extends Exception {
    }

    private static class BufferEmptyException extends Exception {
    }

    public static void main(String[] args) {
        GrumpyBoundedBuffer<Integer> buffer = new GrumpyBoundedBuffer<>(10);
        while (true) {
            try {
                Integer item = buffer.take();
            } catch (BufferEmptyException e) {
                e.printStackTrace();
            }
        }
    }
}
