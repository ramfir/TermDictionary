package com.firdavs.termdictionary

import java.io.File

fun main() {
    //printTerms()
    //printDefinitions()
    //printTranslations()
    newTerms()
}

fun newTerms() {
    val newTerms = File("C:\\Users\\Firdavs\\Desktop\\importTerms.txt").bufferedReader().readLines()
    var subject = ""
    newTerms.forEach {
        val elements = it.split(";")
        if (elements.size == 1 && elements[0].isNotBlank()) {
            subject = elements[0].trim()
            println(subject)
        } else if (elements.size == 3) {
            val name = elements[0].trim()
            val definition = elements[1].trim()
            val translation = elements[2].trim()
            println("$name -- $definition -- $translation -- $subject")
        }
    }
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

