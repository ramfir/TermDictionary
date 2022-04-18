package com.firdavs.termdictionary.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.firdavs.termdictionary.data.room.entity.TermDbEntity
import com.firdavs.termdictionary.domain.model.Term
import kotlinx.coroutines.flow.Flow

@Dao
interface TermsDao {

    @Query("SELECT * FROM terms WHERE (isChosen = :isChosenSelected OR isChosen = 1) AND name LIKE '%' || :searchQuery || '%' ")
    fun getTerms(searchQuery: String, isChosenSelected: Boolean): Flow<List<TermDbEntity>>

    @Update
    suspend fun updateTerm(term: TermDbEntity)

    @Insert
    suspend fun insertTerm(term: TermDbEntity)

    @Query("SELECT * FROM terms WHERE id IN(:randomTermIDs)")
    suspend fun getRandomTerms(randomTermIDs: List<Int>): List<TermDbEntity>
}