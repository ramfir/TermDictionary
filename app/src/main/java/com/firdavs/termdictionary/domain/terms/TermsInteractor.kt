package com.firdavs.termdictionary.domain.terms

import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.domain.repository.TermsRepository
import kotlinx.coroutines.flow.Flow

class TermsInteractor(private val repository: TermsRepository) {

    fun getTerms(searchQuery: String, isChosenSelected: Boolean): Flow<List<Term>> =
        repository.getTerms(searchQuery, isChosenSelected)

    suspend fun updateTerm(term: Term) = repository.updateTerm(term)

    suspend fun getRandomTerms(randomTermIDs: List<Int>) = repository.getRandomTerms(randomTermIDs)
}