package com.amairovi

import io.mockk.*

interface Interface1 {
    fun <RESULT> fun1(stringParam: String, body: () -> RESULT): RESULT
    fun fun2()
}

@Suppress("OVERRIDE_BY_INLINE")
class InlineOverrideTestClass1 : Interface1 {
    lateinit var f1: String
    override inline fun <RESULT> fun1(stringParam: String, body: () -> RESULT): RESULT {
        fun2()
        return body.invoke()
    }

    override fun fun2() {
        f1 = "result"
    }

}

class InlineOverrideTestClass2(private val class1: InlineOverrideTestClass1) {
    fun run(): String {
        return class1.fun1("RESULT") {
            return@fun1 "RESULT 2"
        }
    }
}

class InlineOverrideTestClass3 : Interface1 {
    lateinit var f1: String
    override fun <RESULT> fun1(stringParam: String, body: () -> RESULT): RESULT {
        fun2()
        return body.invoke()
    }

    override fun fun2() {
        f1 = "result"
    }

}

class InlineOverrideTestClass4(private val class3: InlineOverrideTestClass3) {
    fun run(): String {
        return class3.fun1("RESULT") {
            return@fun1 "RESULT 2"
        }
    }
}

class InlineOverrideTestClass5(private val class3: Interface1) {
    fun run(): String {
        return class3.fun1("RESULT") {
            return@fun1 "RESULT 2"
        }
    }
}

fun main() {
    // fun1 doesn't require mocking, fun2 is called directly
    // => method is inlined
    val mockk1 = mockk<InlineOverrideTestClass1>()
    every { mockk1.fun2() } just Runs
    val class2Instance1 = InlineOverrideTestClass2(mockk1)
    print(class2Instance1.run())
//    Exception in thread "main" java.lang.AbstractMethodError: Receiver class kotlin.jvm.functions.Function0$Subclass0 does not define or inherit an implementation of the resolved method 'abstract java.lang.Object invoke()' of interface kotlin.jvm.functions.Function0
//    verify(exactly = 0) { mockk1.fun1(any(), any()) }
    verify(exactly = 1) { mockk1.fun2() }

    // fun1 requires mocking
    // => method is not inlined
    val mockk2 = mockk<InlineOverrideTestClass3>()
    every { mockk2.fun1(any(), any<() -> String>()) } returns "RESULT"
    val class4Instance1 = InlineOverrideTestClass4(mockk2)
    print(class4Instance1.run())
    verify(exactly = 1) { mockk2.fun1(any(), any()) }
    verify(exactly = 0) { mockk2.fun2() }

    // fun1 requires mocking
    // => method is not inlined
    val mockk3 = mockk<Interface1>()
    every { mockk3.fun1(any(), any<() -> String>()) } returns "RESULT"
    val class5Instance1 = InlineOverrideTestClass5(mockk3)
    print(class5Instance1.run())
    verify(exactly = 1) { mockk3.fun1(any(), any()) }
    verify(exactly = 0) { mockk3.fun2() }
}