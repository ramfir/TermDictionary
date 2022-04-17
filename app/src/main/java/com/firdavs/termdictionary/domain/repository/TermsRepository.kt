package com.firdavs.termdictionary.domain.repository

import com.firdavs.termdictionary.domain.model.Term
import kotlinx.coroutines.flow.Flow

interface TermsRepository {

    fun getTerms(): Flow<List<Term>>
    suspend fun addTerm(term: Term)
    suspend fun updateTerm(term: Term)
}