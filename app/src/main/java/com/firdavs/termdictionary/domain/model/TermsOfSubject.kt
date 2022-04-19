package com.firdavs.termdictionary.domain.model

data class TermsOfSubject(
        val subject: Subject,
        val terms: List<Term>
)
