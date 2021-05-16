package com.amairovi;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultDeadlockDetectorTest {

    private final DefaultDeadlockDetector<Number> underTest = new DefaultDeadlockDetector<>();

    @Test
    void whenThereIsNoDeadlockThenDoNothing() {
        Thread thread1 = new Thread();
        Integer id = 1;
        Thread thread2 = new Thread();

        underTest.addLockOwning(id, thread1);

        underTest.check(id, thread2);
    }

    @Test
    void whenThereIsDeadlockThenShouldThrowException() {
        Thread thread1 = new Thread();
        Integer id1 = 1;
        Thread thread2 = new Thread();
        Integer id2 = 2;

        underTest.addLockOwning(id1, thread1);
        underTest.addLockOwning(id2, thread2);

        underTest.addLockAcquiring(id1, thread2);
        underTest.addLockAcquiring(id2, thread1);

        assertThatThrownBy(() -> underTest.check(id2, thread1))
                .isInstanceOf(DeadlockDetectedException.class);
    }

    @Test
    void whenThereIsDeadlockAndSeveralLockAcquiredByBothThreadsThenShouldThrowException() {
        Thread thread1 = new Thread();
        Integer id1 = 1;
        Integer id3 = 3;
        Thread thread2 = new Thread();
        Integer id2 = 2;
        Integer id4 = 4;

        underTest.addLockOwning(id1, thread1);
        underTest.addLockOwning(id3, thread1);
        underTest.addLockOwning(id2, thread2);
        underTest.addLockOwning(id4, thread2);

        underTest.addLockAcquiring(id3, thread2);
        underTest.addLockAcquiring(id2, thread1);

        assertThatThrownBy(() -> underTest.check(id2, thread1))
                .isInstanceOf(DeadlockDetectedException.class);
    }

    @Test
    void whenThereIsChainedDeadlockThenShouldThrowException() {
        int amountOfThreads = 16;
        List<Thread> threads = new ArrayList<>(amountOfThreads);

        for (int i = 0; i < amountOfThreads; i++) {
            threads.add(new Thread());
        }

        for (int id = 0; id < amountOfThreads; id++) {
            underTest.addLockOwning(id, threads.get(id));
            Integer nextId = (id + 1) % amountOfThreads;
            underTest.addLockAcquiring(nextId, threads.get(id));
        }

        for (int i = 0; i < amountOfThreads; i++) {
            int id = i;
            Integer nextId = (id + 1) % amountOfThreads;
            assertThatThrownBy(() -> underTest.check(nextId, threads.get(id)))
                    .isInstanceOf(DeadlockDetectedException.class);
        }
    }

}