package com.firdavs.termdictionary

import java.io.File

fun main() {

    val number = listOf(1, 2, 3, 4, 5, 6, 7, 8)
    val odds = number.filterIndexed { index, _ -> index % 2 == 0}
    val others = number.filterIndexed { index, _ -> index % 2 == 1}
    println(number)
    println(odds)
    println(others)
    //printTerms()
    //printDefinitions()
    //printTranslations()
}

fun printTranslations() {
    val translations = File("app/src/main/assets/translations.txt")
        .readText().split("---")
    translations.forEach {
        println(it)
        println("-------------------------------------------")
    }
    println(translations.size)
}

fun printTerms() {
    val terms: List<String> = File("app/src/main/assets/termsANDtranslations.txt")
        .readLines()
    var i = 0
    while (i < terms.size) {
        println(terms[i])
        i += 2
    }
    println(terms.size)
    println(terms.size/2)
}

fun printDefinitions() {
    val definitions = File("app/src/main/assets/definitions.txt")
        .readText().split("---")
    definitions.forEach {
        println(it)
        println("-------------------------------------------")
    }
    println(definitions.size)
}

