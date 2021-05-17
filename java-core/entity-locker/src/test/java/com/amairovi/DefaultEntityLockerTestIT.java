package com.amairovi;

import com.amairovi.reentrancy.DefaultReentrancyHandler;
import com.amairovi.reentrancy.ReentrancyHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

// TODO: add logging/comments in tests to improve its comprehensibility
class DefaultEntityLockerTestIT {
    // should be greater than 4
    // otherwise, some of tests could fail due to absence of thread to run task within
    private final int amountOfThreads = 16;
    private ExecutorService executor;
    private DefaultEntityLocker<Integer> locker;

    @FunctionalInterface
    private interface Interruptible {
        void run() throws InterruptedException, ExecutionException, BrokenBarrierException;
    }

    @BeforeAll
    static void setup() {
        // TODO: cleanup this mess
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT %1$tL] [%4$-7s] %5$s %n");
        Level level = Level.INFO;
        Arrays.stream(Logger.getGlobal().getParent().getHandlers())
                .forEach(h -> h.setLevel(level));
        Logger.getGlobal().setLevel(level);
        Logger.getLogger("").setLevel(level);
    }


    @BeforeEach
    void beforeEach() {
        ReentrancyHandler<Number> reentrancyHandler = new DefaultReentrancyHandler<>();
        locker = DefaultEntityLocker.<Integer>builder()
                .withReentrancyHandler(reentrancyHandler)
                .build();
        executor = Executors.newFixedThreadPool(amountOfThreads);
    }

    @AfterEach
    void afterEach() {
        shutdownAndAwaitTermination(executor);
    }

    @RepeatedTest(100)
    @Timeout(5)
    void whenThereAreSeveralThreadsForSameEntityThenAtMostOneIsAllowedToExecuteProtectedCode() {
        int amountOfOperations = 1000;
        List<Integer> idsToIncrement = new Random().ints(amountOfOperations, 0, amountOfThreads / 2)
                .boxed()
                .collect(toList());
        Map<Integer, Long> expectedResult = idsToIncrement.stream()
                .collect(groupingBy(identity(), counting()));

        Map<Integer, Long> actualResult = new ConcurrentHashMap<>();
        try {
            List<Callable<Integer>> collect = idsToIncrement.stream()
                    .map(idToIncrement -> (Callable<Integer>) () -> {
                        locker.lock(idToIncrement);
                        unsafeOperation(actualResult, idToIncrement);
                        locker.unlock(idToIncrement);
                        return idToIncrement;
                    })
                    .collect(toList());

            for (Future<Integer> future : executor.invokeAll(collect)) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            fail(e);
        }

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @Timeout(5)
    void whenThereAreSeveralEntitiesToLockBySameThreadThenLockThemExclusively() {
        int amountOfEntities = 3;

        List<AtomicInteger> counters = new ArrayList<>(amountOfEntities);
        List<CountDownLatch> latches = new ArrayList<>(amountOfEntities);
        List<Future<?>> updateFromAnotherThreadFuture = new ArrayList<>(amountOfEntities);

        for (int i = 0; i < amountOfEntities; i++) {
            Integer id = i;
            AtomicInteger counter = new AtomicInteger();
            counters.add(counter);
            CountDownLatch latch = new CountDownLatch(1);
            latches.add(latch);
            locker.lock(id);

            updateFromAnotherThreadFuture.add(
                    executor.submit(() -> {
                        locker.lock(id);
                        latch.countDown();
                        counter.incrementAndGet();
                        locker.unlock(id);
                    })
            );
        }

        for (int id = 0; id < amountOfEntities; id++) {
            CountDownLatch latch = latches.get(id);
            runInterruptibleSafely(() -> assertThat(latch.await(1, TimeUnit.SECONDS)).isFalse());
            AtomicInteger counter = counters.get(id);
            counter.incrementAndGet();
            assertThat(counter.get()).isEqualTo(1);
            locker.unlock(id);
            runInterruptibleSafely(updateFromAnotherThreadFuture.get(id)::get);
            assertThat(counter.get()).isEqualTo(2);
        }
    }

    @Test
    @Timeout(5)
    void whenThereAreDifferentEntitiesThenTheyCanBeLockedConcurrently() {
        CountDownLatch latch = new CountDownLatch(amountOfThreads);

        // amount of entities == amount of threads
        // so each thread should count down once to proceed
        IntStream.range(0, amountOfThreads)
                .mapToObj(id -> (Runnable) () -> {
                    locker.lock(id);
                    latch.countDown();
                    runInterruptibleSafely(latch::await);

                    locker.unlock(id);
                })
                .forEach(executor::submit);

        runInterruptibleSafely(
                () -> assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue()
        );
    }

    @Test
    @Timeout(5)
    void whenReentrantLockTakesPlaceThenItShouldSucceed() {
        AtomicInteger counter = new AtomicInteger(0);

        CountDownLatch latch = new CountDownLatch(1);
        Integer id = 1;
        locker.lock(id);

        locker.lock(id);
        counter.incrementAndGet();
        locker.unlock(id);
        assertThat(counter.get()).isEqualTo(1);

        Future<?> future = executor.submit(() -> {
            locker.lock(id);
            latch.countDown();
            counter.incrementAndGet();
            locker.unlock(id);
        });

        counter.incrementAndGet();
        runInterruptibleSafely(() -> assertThat(latch.await(1, TimeUnit.SECONDS)).isFalse());
        assertThat(counter.get()).isEqualTo(2);
        locker.unlock(id);

        runInterruptibleSafely(future::get);

        assertThat(counter.get()).isEqualTo(3);
    }

    @Test
    @Timeout(5)
    void whenLockIsNotAcquiredBeforeTimeoutThenReturnFalse() throws ExecutionException, InterruptedException {
        int id = 1;
        locker.lock(id);
        Future<Boolean> lockFromAnotherThreadFuture = executor.submit(
                () -> locker.lock(id, 1, TimeUnit.SECONDS)
        );
        assertThat(lockFromAnotherThreadFuture.get()).isFalse();

        locker.unlock(id);
    }

    @Test
    @Timeout(5)
    void whenLockIsAcquiredBeforeTimeoutThenReturnTrue() {
        int id = 1;

        CountDownLatch latch = new CountDownLatch(1);
        executor.submit(() -> {
            locker.lock(id);
            latch.countDown();
            runInterruptibleSafely(() -> Thread.sleep(1000));
            locker.unlock(id);
        });

        assertThat(locker.lock(1, 2, TimeUnit.SECONDS)).isTrue();
    }

    @Test
    @Timeout(5)
    void whenThereIsDeadlockThenShouldThrowExceptionAndKeepAlreadyAcquiredLocks() {
        // given
        int id1 = 1;
        int id2 = 2;

        CyclicBarrier barrier = new CyclicBarrier(2);

        // when
        Future<?> future1 = executor.submit(() -> {
            locker.lock(id1);
            runInterruptibleSafely(barrier::await);
            locker.lock(id2);
        });

        Future<?> future2 = executor.submit(() -> {
            locker.lock(id2);
            runInterruptibleSafely(barrier::await);
            locker.lock(id1);
        });

        // then
        assertThatThrownBy(future1::get)
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(DeadlockDetectedException.class);

        assertThatThrownBy(future2::get)
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(DeadlockDetectedException.class);
        assertThat(locker.lock(id1, 100, TimeUnit.MILLISECONDS)).isFalse();
        assertThat(locker.lock(id2, 100, TimeUnit.MILLISECONDS)).isFalse();
    }

    @Test
    @Timeout(5)
    void whenThereIsDeadlockThenShouldNotInfluenceOnFurtherLockTry() {
        // given
        int id1 = 1;
        int id2 = 2;

        CyclicBarrier barrier = new CyclicBarrier(2);

        CountDownLatch latch = new CountDownLatch(1);
        // when
        executor.submit(() -> {
            locker.lock(id1);
            runInterruptibleSafely(barrier::await);
            try {
                locker.lock(id2);
            } catch (DeadlockDetectedException ignore) {
                latch.countDown();
            }
        });

        executor.submit(() -> {
            locker.lock(id2);
            runInterruptibleSafely(barrier::await);
            try {
                locker.lock(id1);
            } catch (DeadlockDetectedException ignore) {
                latch.countDown();
            }
        });

        // then
        runInterruptibleSafely(latch::await);
        assertThat(locker.lock(id1, 100, TimeUnit.MILLISECONDS)).isFalse();
        assertThat(locker.lock(id2, 100, TimeUnit.MILLISECONDS)).isFalse();
    }

    @Test
    @Timeout(5)
    void whenThereIsDeadlockAndLockWithTimeoutIsUsedThenShouldNotThrowException() {
        // given
        int id1 = 1;
        int id2 = 2;

        CyclicBarrier barrier = new CyclicBarrier(2);

        // when
        Future<?> future1 = executor.submit(() -> {
            locker.lock(id1);
            runInterruptibleSafely(barrier::await);
            locker.lock(id2, 100, TimeUnit.MILLISECONDS);
        });

        Future<?> future2 = executor.submit(() -> {
            locker.lock(id2);
            runInterruptibleSafely(barrier::await);
            locker.lock(id1, 100, TimeUnit.MILLISECONDS);
        });

        // then
        assertDoesNotThrow(() -> future1.get());
        assertDoesNotThrow(() -> future2.get());
        assertThat(locker.lock(id1, 100, TimeUnit.MILLISECONDS)).isFalse();
        assertThat(locker.lock(id2, 100, TimeUnit.MILLISECONDS)).isFalse();
    }

    @Test
    @Timeout(5)
    void whenThereIsChainedDeadlockThenShouldThrowException() {
        List<Future<?>> futures = new ArrayList<>();
        CyclicBarrier barrier = new CyclicBarrier(amountOfThreads);
        for (int id = 0; id < amountOfThreads; id++) {
            Integer nextId = (id + 1) % amountOfThreads;
            Integer effectiveId = id;
            Future<?> future = executor.submit(() -> {
                locker.lock(effectiveId);
                runInterruptibleSafely(barrier::await);
                try {
                    locker.lock(nextId);
                } finally {
                    locker.unlock(effectiveId);
                }
            });
            futures.add(future);
        }

        AtomicInteger amountOfDetectedDeadlocks = new AtomicInteger();
        for (int i = 0; i < amountOfThreads; i++) {
            try {
                futures.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                assertThat(e).isInstanceOf(ExecutionException.class)
                        .hasCauseInstanceOf(DeadlockDetectedException.class);
                amountOfDetectedDeadlocks.incrementAndGet();
            }
        }
        assertThat(amountOfDetectedDeadlocks.get()).isGreaterThan(0);
    }

    @Test
    @Timeout(5)
    void whenThereAreNoOtherLocksThenShouldSuccessfullyAcquireGlobalLockAndPreventOtherThreadsFromAcquirementOfAnyLocks() throws InterruptedException {
        locker.lockGlobal();

        AtomicInteger counter = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(1);
        Future<?> future1 = executor.submit(() -> {
            int id1 = 1;
            locker.lock(id1);
            latch.countDown();
            counter.getAndIncrement();
        });

        assertThat(latch.await(100, TimeUnit.MILLISECONDS)).isFalse();
        assertThat(counter.get()).isEqualTo(0);
        locker.unlockGlobal();

        runInterruptibleSafely(future1::get);
        assertThat(counter.get()).isEqualTo(1);
    }

    @Test
    @Timeout(5)
    void whenThereLockAcquiredByAnotherThreadThenShouldNotSuccessfullyAcquireGlobalLockUntilUnlock() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);
        Future<?> future1 = executor.submit(() -> {
            int id1 = 1;
            locker.lock(id1);
            latch.countDown();
            runInterruptibleSafely(() -> assertThat(latch2.await(100, TimeUnit.MILLISECONDS)).isFalse());
            assertThat(counter.getAndIncrement()).isEqualTo(0);
            locker.unlock(id1);
        });

        Future<?> future2 = executor.submit(() -> {
            runInterruptibleSafely(latch::await);
            locker.lockGlobal();
            latch2.countDown();
            assertThat(counter.getAndIncrement()).isEqualTo(1);
            locker.unlockGlobal();
        });
        runInterruptibleSafely(future1::get);
        runInterruptibleSafely(future2::get);
        assertThat(counter.get()).isEqualTo(2);
    }

    @Test
    @Timeout(5)
    void whenThreadHasGlobalLockThenItShouldBeAbleToAcquireLock() {
        CountDownLatch latch = new CountDownLatch(1);
        Future<?> future = executor.submit(() -> {
            locker.lockGlobal();
            int id1 = 1;
            locker.lock(id1);
            latch.countDown();
        });

        runInterruptibleSafely(() -> assertThat(latch.await(100, TimeUnit.MILLISECONDS)).isTrue());
        assertDoesNotThrow((ThrowingSupplier<?>) future::get);
    }

    @Test
    @Timeout(5)
    void whenAcquireGlobalWhichHasBeenAlreadyAcquiredThenShouldSucceed() {
        locker.lockGlobal();
        locker.lockGlobal();
        locker.unlockGlobal();
        locker.unlockGlobal();
    }

    @Test
    @Timeout(5)
    void whenUnlockNotLockedGlobalThenDoNothing() {
        locker.unlockGlobal();
    }

    @Test
    @Timeout(5)
    void whenGlobalLockIsAcquiredThenConcurrentAcquiringOfGlobalShouldThrowExceptionIfThreadLockedSomeEntitiesAndShouldDoNothingIfItDoesNot() {
        AtomicInteger counter = new AtomicInteger(0);

        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);

        Future<?> future1 = executor.submit(() -> {
            int id = 1;
            locker.lock(id);
            assertThat(counter.getAndIncrement()).isEqualTo(0);
            latch.countDown();
            runInterruptibleSafely(() -> assertThat(latch2.await(100, TimeUnit.MILLISECONDS)).isFalse());
            assertThat(counter.getAndIncrement()).isEqualTo(2);
            assertThatThrownBy(() -> locker.lockGlobal()).isInstanceOf(DeadlockDetectedException.class);
            assertThat(counter.getAndIncrement()).isEqualTo(3);
            locker.unlock(id);
        });

        Future<?> future2 = executor.submit(() -> {
            runInterruptibleSafely(latch::await);
            runInterruptibleSafely(() -> assertThat(latch2.await(200, TimeUnit.MILLISECONDS)).isTrue());
            assertThat(counter.getAndIncrement()).isEqualTo(5);
            locker.lockGlobal();
            assertThat(counter.getAndIncrement()).isEqualTo(7);
            locker.unlockGlobal();
        });

        Future<?> future3 = executor.submit(() -> {
            runInterruptibleSafely(latch::await);
            assertThat(counter.getAndIncrement()).isEqualTo(1);
            locker.lockGlobal();
            assertThat(counter.getAndIncrement()).isEqualTo(4);
            latch2.countDown();
            runInterruptibleSafely(() -> Thread.sleep(200));
            locker.unlockGlobal();
            assertThat(counter.getAndIncrement()).isEqualTo(6);
        });

        assertDoesNotThrow((ThrowingSupplier<?>) future1::get);
        assertDoesNotThrow((ThrowingSupplier<?>) future2::get);
        assertDoesNotThrow((ThrowingSupplier<?>) future3::get);
    }

    @Test
    @Timeout(5)
    void whenAThreadWhichTriesToLockEntityAcquiredByAnotherThreadWhichTriesToLockGlobalLockThenShouldThrowDeadlockException() {
        CountDownLatch latch = new CountDownLatch(2);
        int commonId = 1;
        Future<?> future1 = executor.submit(() -> {
            locker.lock(commonId);
            latch.countDown();
            runInterruptibleSafely(latch::await);
            locker.lockGlobal();
        });

        Future<?> future2 = executor.submit(() -> {
            int anotherId = 2;
            locker.lock(anotherId);
            latch.countDown();
            runInterruptibleSafely(latch::await);
            try {
                locker.lock(commonId);
            } catch (DeadlockDetectedException e) {
                // TODO add message check
                locker.unlock(anotherId);
                return;
            }
            fail("DeadlockDetectedException was expected");
        });
        assertDoesNotThrow((ThrowingSupplier<?>) future1::get);
        assertDoesNotThrow((ThrowingSupplier<?>) future2::get);
    }

    @Test
    @Timeout(5)
    void whenThereIsAGlobalLockAndOtherThreadDoesNotHaveAnyEntitiesAcquireThenShouldNotThrowException() {
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);
        Future<?> future1 = executor.submit(() -> {
            locker.lockGlobal();
            latch.countDown();
            runInterruptibleSafely(() -> assertThat(latch2.await(100, TimeUnit.MILLISECONDS)).isFalse());
            locker.unlockGlobal();
        });

        Future<?> future2 = executor.submit(() -> {
            runInterruptibleSafely(latch::await);
            int anotherId = 2;
            locker.lock(anotherId);
            latch2.countDown();
        });
        assertDoesNotThrow((ThrowingSupplier<?>) future1::get);
        assertDoesNotThrow((ThrowingSupplier<?>) future2::get);
    }

    private void runInterruptibleSafely(final Interruptible interruptible) {
        try {
            interruptible.run();
        } catch (InterruptedException e) {
            fail("Should not be interrupted", e);
        } catch (ExecutionException e) {
            fail("Should be executed successfully", e);
        } catch (BrokenBarrierException e) {
            fail("Should not be broken", e);
        }
    }

    private void unsafeOperation(final Map<Integer, Long> actualResult, final Integer idToIncrement) {
        Long val = actualResult.getOrDefault(idToIncrement, 0L);
        actualResult.put(idToIncrement, val + 1);
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}