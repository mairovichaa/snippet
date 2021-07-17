package com.amairovi

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    val start = System.currentTimeMillis()
    val deferred = (1..1_000_000).map { n ->
        GlobalScope.async {
//            delay is needed to check if computations are evaluated in parallel
//            sequential execution would take over 11,5 days
            delay(1000)
            n
        }
    }
    runBlocking {
        val sum = deferred.sumOf { it.await().toLong() }
        val end = System.currentTimeMillis()
        println("Sum: $sum")
        println("Execution took ${end - start} millis")
    }

}