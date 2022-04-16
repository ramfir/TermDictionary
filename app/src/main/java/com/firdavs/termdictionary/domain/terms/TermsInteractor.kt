package com.firdavs.termdictionary.domain.terms

import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.domain.repository.TermsRepository
import kotlinx.coroutines.flow.Flow

class TermsInteractor(private val termsRepository: TermsRepository) {

    fun getTerms(): Flow<List<Term>> = termsRepository.getTerms()
}