package com.amairovi;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DefaultDeadlockDetectorTest {

    private final DefaultDeadlockDetector<Number> underTest = new DefaultDeadlockDetector<>();

    @Test
    void whenThereIsNoDeadlockThenDoNothing() {
        Thread thread1 = new Thread();
        Integer id = 1;
        Thread thread2 = new Thread();

        LockingContext<Number> context = new LockingContext<>();
        context.addLockOwning(id, thread1);

        underTest.check(id, thread2, context);
    }

    @Test
    void whenThereIsDeadlockThenShouldThrowException() {
        Thread thread1 = new Thread();
        Integer id1 = 1;
        Thread thread2 = new Thread();
        Integer id2 = 2;

        LockingContext<Number> context = new LockingContext<>();
        context.addLockOwning(id1, thread1);
        context.addLockOwning(id2, thread2);

        context.addLockAcquiring(id1, thread2);
        context.addLockAcquiring(id2, thread1);

        assertThatThrownBy(() -> underTest.check(id2, thread1, context))
                .isInstanceOf(DeadlockDetectedException.class);
    }

    @Test
    void whenThereIsDeadlockInvolvingGlobalLockThenShouldThrowException() {
        Thread thread1 = new Thread();
        Integer id1 = 1;
        Thread thread2 = new Thread();
        Integer id2 = 2;

        LockingContext<Number> context = new LockingContext<>();
        context.addLockOwning(id1, thread1);
        context.addLockOwning(id2, thread2);
        context.addGlobalLockAcquiring(thread1);

        assertThatThrownBy(() -> underTest.check(id1, thread2,context))
                .isInstanceOf(DeadlockDetectedException.class);
    }

    @Test
    void whenThereIsAGlobalLockAndOtherThreadDoesNotHaveAnyEntitiesAcquireThenShouldNotThrowException() {
        Thread thread1 = new Thread();
        Thread thread2 = new Thread();

        Integer id1 = 1;
        LockingContext<Number> context = new LockingContext<>();
        context.addLockOwning(id1, thread1);
        context.addGlobalLockAcquiring(thread1);

        assertDoesNotThrow(() -> underTest.check(id1, thread2, context));
    }

    @Test
    void whenThereIsDeadlockAndSeveralLockAcquiredByBothThreadsThenShouldThrowException() {
        Thread thread1 = new Thread();
        Integer id1 = 1;
        Integer id3 = 3;
        Thread thread2 = new Thread();
        Integer id2 = 2;
        Integer id4 = 4;

        LockingContext<Number> context = new LockingContext<>();
        context.addLockOwning(id1, thread1);
        context.addLockOwning(id3, thread1);
        context.addLockOwning(id2, thread2);
        context.addLockOwning(id4, thread2);

        context.addLockAcquiring(id3, thread2);
        context.addLockAcquiring(id2, thread1);

        assertThatThrownBy(() -> underTest.check(id2, thread1, context))
                .isInstanceOf(DeadlockDetectedException.class);
    }

    @Test
    void whenThereIsChainedDeadlockThenShouldThrowException() {
        int amountOfThreads = 16;
        List<Thread> threads = new ArrayList<>(amountOfThreads);

        for (int i = 0; i < amountOfThreads; i++) {
            threads.add(new Thread());
        }

        LockingContext<Number> context = new LockingContext<>();
        for (int id = 0; id < amountOfThreads; id++) {
            context.addLockOwning(id, threads.get(id));
            Integer nextId = (id + 1) % amountOfThreads;
            context.addLockAcquiring(nextId, threads.get(id));
        }

        for (int i = 0; i < amountOfThreads; i++) {
            int id = i;
            Integer nextId = (id + 1) % amountOfThreads;
            assertThatThrownBy(() -> underTest.check(nextId, threads.get(id), context))
                    .isInstanceOf(DeadlockDetectedException.class);
        }
    }

}