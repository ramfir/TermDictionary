package com.firdavs.termdictionary.domain.model

data class SubjectsOfTerm(
        val term: Term,
        val subjects: List<Subject>
)
