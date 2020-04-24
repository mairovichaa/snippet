package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_5;

import com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_5.Task9.ReentrantLockAutoCloseable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReentrantLock;

import static com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_5.Task9.lock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task9Test {

    @Test
    void test() {
        assertThatThrownBy(() -> {
            ReentrantLock reentrantLock = new ReentrantLock();
            try (ReentrantLockAutoCloseable closeable = lock(reentrantLock)) {
                try (ReentrantLockAutoCloseable closeable2 = lock(reentrantLock)) {
                    assertThat(reentrantLock.getHoldCount()).isEqualTo(2);

                    throw new Exception("throw exception to check that autoclosable works");
                } finally {
                    assertThat(reentrantLock.getHoldCount()).isEqualTo(1);

                }
            } finally {
                assertThat(reentrantLock.getHoldCount()).isEqualTo(0);

            }
        }).hasMessage("throw exception to check that autoclosable works");
    }

}
