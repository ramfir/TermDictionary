package com.firdavs.termdictionary.domain.repository

import com.firdavs.termdictionary.domain.model.SubjectsOfTerm
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.domain.model.TermsOfSubject
import kotlinx.coroutines.flow.Flow

interface TermsRepository {

    fun getTerms(searchQuery: String, isChosenSelected: Boolean): Flow<List<Term>>

    suspend fun addTerm(term: Term)

    suspend fun updateTerm(term: Term)

    suspend fun getRandomTerms(randomTermIDs: List<Int>): List<Term>

    fun getTermsOfSubject(subjectName: String): Flow<TermsOfSubject>

    fun getSubjectsOfTerm(termId: Long): Flow<SubjectsOfTerm>

    suspend fun insertTerm(term: Term): Long

    suspend fun insertTermSubjectConnection(termId: Long, subjectId: Long)
}