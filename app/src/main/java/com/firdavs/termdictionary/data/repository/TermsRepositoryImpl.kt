package com.firdavs.termdictionary.data.repository

import com.firdavs.termdictionary.data.room.TermsDao
import com.firdavs.termdictionary.data.room.entity.TermDbEntity
import com.firdavs.termdictionary.data.room.entity.toDomain
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.domain.repository.TermsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TermsRepositoryImpl(
    private val termsDao: TermsDao
): TermsRepository {

    override fun getTerms(): Flow<List<Term>> {
        return termsDao.getTerms().map {
            it.toDomain()
        }
    }

    override suspend fun addTerm(term: Term) {
        termsDao.insertTerm(TermDbEntity.fromTerm(term))
    }

    override suspend fun updateNotes(term: Term) {
        termsDao.updateNotes(TermDbEntity.fromTerm(term))
    }
}