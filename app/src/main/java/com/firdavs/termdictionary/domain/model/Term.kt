package com.firdavs.termdictionary.domain.model

data class Term(
        val id: Long,
        val name: String,
        val definition: String,
        val translation: String,
        val notes: String,
        val isChosen: Boolean
)