package com.amairovi.goetz_concurrency_in_practice.chapter_10;

import com.amairovi.goetz_concurrency_in_practice.chapter_10.MoneyTransfering.Account;

import java.util.Random;

import static com.amairovi.goetz_concurrency_in_practice.chapter_10.MoneyTransfering.transferMoney;

public class DemonstrateDeadlock {

    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1_000_000;

    public static void main(String[] args) {
        final Random rand = new Random();
        final Account[] accounts = new Account[NUM_ACCOUNTS];

        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account();
        }

        class TransferThread extends Thread {


            @Override
            public void run() {
                String threadName = Thread.currentThread().getName();
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    System.out.println(threadName + ": iteration #" + i);
                    int fromAcc = rand.nextInt(NUM_ACCOUNTS);
                    int toAcc = rand.nextInt(NUM_ACCOUNTS);
                    long amount = Math.abs(rand.nextInt());

                    transferMoney(accounts[fromAcc], accounts[toAcc], amount);
                }
            }

        }
        for (int i = 0; i < NUM_THREADS; i++) {
            new TransferThread().start();
        }
    }

}
