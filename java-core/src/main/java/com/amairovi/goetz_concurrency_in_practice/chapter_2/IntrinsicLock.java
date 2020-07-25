package com.amairovi.goetz_concurrency_in_practice.chapter_2;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IntrinsicLock {



    public IntrinsicLock() {
        synchronized (this) {

        }
    }


    public synchronized void sync() {
//    uses this as monitor
    }


    public void sync2() throws InterruptedException {
        synchronized (this) {
            this.wait();
        }
    }


    public void sync3() throws InterruptedException {
        this.wait(1000);
    }

    public static void main(String[] args) {
        IntrinsicLock lock = new IntrinsicLock();

        assertThatThrownBy(lock::sync3).isInstanceOf(IllegalMonitorStateException.class);
        assertThatCode(() -> {
            synchronized (lock) {
                lock.sync3();
            }
        }).doesNotThrowAnyException();
    }

}
