package com.example.termdictionary.domain.model

data class Question(
    val text: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val answer: String
)