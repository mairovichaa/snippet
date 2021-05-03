package com.amairovi;

import org.junit.jupiter.api.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class DefaultEntityLockerTest {

    private DefaultEntityLocker<Integer> locker;

    @FunctionalInterface
    private interface Interruptible {
        void run() throws InterruptedException, ExecutionException;
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
        locker = new DefaultEntityLocker<>(reentrancyHandler);
    }

    @RepeatedTest(100)
    @Timeout(5)
    void whenThereAreSeveralThreadsForSameEntityThenAtMostOneIsAllowedToExecuteProtectedCode() {
        int amountOfThreads = 8;
        int amountOfOperations = 1000;
        List<Integer> idsToIncrement = new Random().ints(amountOfOperations, 0, amountOfThreads / 2)
                .boxed()
                .collect(toList());
        Map<Integer, Long> expectedResult = idsToIncrement.stream()
                .collect(groupingBy(identity(), counting()));

        ExecutorService executorService = Executors.newFixedThreadPool(amountOfThreads);
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

            for (Future<Integer> future : executorService.invokeAll(collect)) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            fail(e);
        } finally {
            shutdownAndAwaitTermination(executorService);
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
        ExecutorService executorService = Executors.newFixedThreadPool(amountOfEntities);

        for (int i = 0; i < amountOfEntities; i++) {
            Integer id = i;
            AtomicInteger counter = new AtomicInteger();
            counters.add(counter);
            CountDownLatch latch = new CountDownLatch(1);
            latches.add(latch);
            locker.lock(id);

            updateFromAnotherThreadFuture.add(
                    executorService.submit(() -> {
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
        int amountOfThreads = 8;

        CountDownLatch latch = new CountDownLatch(amountOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(amountOfThreads);
        try {

            // amount of entities == amount of threads
            // so each thread should count down once to proceed
            IntStream.range(0, amountOfThreads)
                    .mapToObj(id -> (Runnable) () -> {
                        locker.lock(id);
                        latch.countDown();
                        runInterruptibleSafely(latch::await);

                        locker.unlock(id);
                    })
                    .forEach(executorService::submit);
        } finally {
            shutdownAndAwaitTermination(executorService);
        }
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

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
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
        CompletableFuture<Boolean> lockFromAnotherThreadFuture = CompletableFuture.supplyAsync(
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
        CompletableFuture.runAsync(() -> {
            locker.lock(id);
            latch.countDown();
            runInterruptibleSafely(() -> Thread.sleep(1000));
            locker.unlock(id);
        });

        assertThat(locker.lock(1, 2, TimeUnit.SECONDS)).isTrue();
    }

    private void runInterruptibleSafely(final Interruptible interruptible) {
        try {
            interruptible.run();
        } catch (InterruptedException e) {
            fail("Should not be interrupted", e);
        } catch (ExecutionException e) {
            fail("Should be executed successfully", e);
        }
    }

    private void unsafeOperation(final Map<Integer, Long> actualResult, final Integer idToIncrement) {
        Long val = actualResult.getOrDefault(idToIncrement, 0L);
        actualResult.put(idToIncrement, val + 1);
    }

    void shutdownAndAwaitTermination(ExecutorService pool) {
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