package com.amairovi.goetz_concurrency_in_practice.chapter_12;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class BoundedBufferTest {

    @Test
    void testIsEmptyWhenConstucted() {
        BoundedBuffer<Integer> underTest = new BoundedBuffer<>(10);
        assertTrue(underTest.isEmpty());
        assertFalse(underTest.isFull());
    }

    @Test
    void testIsFullAfterPuts() throws InterruptedException {
        BoundedBuffer<Integer> underTest = new BoundedBuffer<>(10);
        for (int i = 0; i < 10; i++) {
            underTest.put(i);
        }
        assertFalse(underTest.isEmpty());
        assertTrue(underTest.isFull());
    }

    @Test
    void testTakeBlocksWhenEmpty() {
        final BoundedBuffer<Integer> underTest = new BoundedBuffer<>(10);
        AtomicBoolean result = new AtomicBoolean();
        Thread taker = new Thread(() -> {
            try {
                int unused = underTest.take();
                result.set(true);
            } catch (InterruptedException success) {
            }

        });

        try {
            int LOCKUP_DETECT_TIMEOUT = 4 * 1000;
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive());
            assertFalse(result.get());
        } catch (Exception e) {
            fail();
        }
    }

}