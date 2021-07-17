package com.amairovi

class SeveralLevelsOfInlineTestClass1 {
    inline fun inlineLevel1(body: () -> Unit) {
        println("inlineLevel1")
        inlineLevel2(body)
    }

    inline fun inlineLevel2(body: () -> Unit) {
        println("inlineLevel2")
        inlineLevel3(body)
    }

    inline fun inlineLevel3(body: () -> Unit) {
        println("inlineLevel3")
        body.invoke()
    }
}

fun main() {
    val instance = SeveralLevelsOfInlineTestClass1()
    instance.inlineLevel1 { println("body") }
}