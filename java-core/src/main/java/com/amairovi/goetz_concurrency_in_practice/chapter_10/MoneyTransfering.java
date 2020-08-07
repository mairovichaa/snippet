package com.amairovi.goetz_concurrency_in_practice.chapter_10;

public class MoneyTransfering {


    // deadlock-prone!
    static void transferMoney(Account fromAccount, Account toAccount, long amount) {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                transfer(fromAccount, toAccount, amount);
            }
        }
    }


    private static final Object tieLock = new Object();

    public void transferMoneySafe(final Account from, final Account to, final long amount) {
        int fromHash = System.identityHashCode(from);
        int toHash = System.identityHashCode(to);

        if (fromHash == toHash) {
            synchronized (tieLock) {
                synchronized (from) {
                    synchronized (to) {
                        transfer(from, to, amount);
                    }
                }
            }
            return;
        }

        if (fromHash < toHash){
            synchronized (from) {
                synchronized (to) {
                    transfer(from, to, amount);
                }
            }
        } else {
            synchronized (to) {
                synchronized (from) {
                    transfer(from, to, amount);
                }
            }
        }
    }

    private static void transfer(Account fromAccount, Account toAccount, long amount) {
        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException();
        } else {
            fromAccount.debit(amount);
            toAccount.credit(amount);
        }
    }

    static class Account {

        public long getBalance() {
            return Long.MAX_VALUE;
        }

        public void debit(long amount) {

        }

        public void credit(long amount) {

        }

    }

}
