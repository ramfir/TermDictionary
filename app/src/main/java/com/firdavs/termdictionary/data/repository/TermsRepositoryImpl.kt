package com.firdavs.termdictionary.data.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.firdavs.termdictionary.data.model.toDomain
import com.firdavs.termdictionary.data.room.dao.TermsDao
import com.firdavs.termdictionary.data.room.entity.TermSubjectDbEntity
import com.firdavs.termdictionary.data.room.entity.toData
import com.firdavs.termdictionary.data.room.entity.toDomain
import com.firdavs.termdictionary.domain.model.SubjectsOfTerm
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.domain.model.TermsOfSubject
import com.firdavs.termdictionary.domain.repository.TermsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TermsRepositoryImpl(
        private val termsDao: TermsDao,
) : TermsRepository {

    override fun getTerms(searchQuery: String, isChosenSelected: Boolean): Flow<List<Term>> {
        return termsDao.getTerms(searchQuery, isChosenSelected).map { it.toDomain() }
    }

    override suspend fun getTermId(name: String, definition: String): Long {
        return termsDao.getTermId(name, definition)
    }

    override suspend fun insertTerm(term: Term): Long {
        return termsDao.insertTerm(term.toData())
    }

    override suspend fun insertTermSubjectConnection(termId: Long, subjectId: Long) {
        try {
            termsDao.insertTermSubject(TermSubjectDbEntity(termId, subjectId))
        } catch(e: SQLiteConstraintException) {
            Log.d("MyApp", "${e.stackTrace}")
        }
    }

    override suspend fun addTerm(term: Term) {
        termsDao.insertTerm(term.toData())
    }

    override suspend fun updateTerm(term: Term) {
        termsDao.updateTerm(term.toData())
    }

    override suspend fun getRandomTerms(randomTermIDs: List<Int>): List<Term> {
        return termsDao.getRandomTerms(randomTermIDs).map { it.toDomain() }
    }

    override fun getTermsOfSubject(subjectName: String): Flow<TermsOfSubject> {
        return termsDao.getTermsOfSubject(subjectName).map { it.toDomain() }
    }

    override fun getSubjectsOfTerm(termId: Long): Flow<SubjectsOfTerm> {
        return termsDao.getSubjectsOfTerm(termId).map { it.toDomain() }
    }
}