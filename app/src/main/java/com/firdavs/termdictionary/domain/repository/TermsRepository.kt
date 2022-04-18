package com.firdavs.termdictionary.domain.repository

import com.firdavs.termdictionary.domain.model.Term
import kotlinx.coroutines.flow.Flow

interface TermsRepository {

    fun getTerms(searchQuery: String, isChosenSelected: Boolean): Flow<List<Term>>
    suspend fun addTerm(term: Term)
    suspend fun updateTerm(term: Term)
    suspend fun getRandomTerms(randomTermIDs: List<Int>): List<Term>
}