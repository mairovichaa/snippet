package com.amairovi.goetz_concurrency_in_practice.chapter_5.cache;

import java.math.BigInteger;

public class ExpensiveFunction implements Computable<String, BigInteger> {

    @Override
    public BigInteger compute(String arg) {
        //a lot of time...
        return new BigInteger(arg);
    }

}
