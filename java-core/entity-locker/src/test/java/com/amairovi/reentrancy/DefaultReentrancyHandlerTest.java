package com.amairovi.reentrancy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultReentrancyHandlerTest {

    private final ReentrancyHandler<Number> reentrancyHandler = new DefaultReentrancyHandler<>();

    @Test
    void whenLockWasNotAcquiredBeforeShouldIncreaseAmountOfReentrancy() {
        reentrancyHandler.increase(1);
        reentrancyHandler.increase(1L);
        reentrancyHandler.increase(1.0);

        assertThat(reentrancyHandler.getReentrancyNumber(1)).isEqualTo(1);
        assertThat(reentrancyHandler.getReentrancyNumber(1L)).isEqualTo(1);
        assertThat(reentrancyHandler.getReentrancyNumber(1.0)).isEqualTo(1);

        reentrancyHandler.increase(1);
        assertThat(reentrancyHandler.getReentrancyNumber(1)).isEqualTo(2);
        assertThat(reentrancyHandler.getReentrancyNumber(1L)).isEqualTo(1);
        assertThat(reentrancyHandler.getReentrancyNumber(1.0)).isEqualTo(1);
    }

    @Test
    void whenLockWasNotAcquiredBeforeShouldNotIncreaseAmountOfReentrancy() {
        double randomId = Math.random();
        assertThat(reentrancyHandler.increaseIfPresent(randomId)).isFalse();
        assertThat(reentrancyHandler.getReentrancyNumber(randomId)).isNull();
    }

    @Test
    void whenLockWasAcquiredBeforeShouldIncreaseAmountOfReentrancy() {
        double randomId = Math.random();
        reentrancyHandler.increase(randomId);
        assertThat(reentrancyHandler.getReentrancyNumber(randomId)).isEqualTo(1);

        assertThat(reentrancyHandler.increaseIfPresent(randomId)).isTrue();
        assertThat(reentrancyHandler.getReentrancyNumber(randomId)).isEqualTo(2);
    }

    @Test
    void whenLockWasNotAcquiredBeforeShouldReturnFalseOnGet() {
        assertThat(reentrancyHandler.getReentrancyNumber(Math.random())).isNull();
    }
}