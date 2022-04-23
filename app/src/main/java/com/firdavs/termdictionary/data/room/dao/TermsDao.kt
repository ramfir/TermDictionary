package com.firdavs.termdictionary.data.room.dao

import androidx.room.*
import com.firdavs.termdictionary.data.model.SubjectsOfTermDb
import com.firdavs.termdictionary.data.model.TermsOfSubjectDb
import com.firdavs.termdictionary.data.room.entity.TermDbEntity
import com.firdavs.termdictionary.data.room.entity.TermSubjectDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TermsDao {

    @Query("SELECT * FROM terms WHERE (isChosen = :isChosenSelected OR isChosen = 1) AND name LIKE '%' || :searchQuery || '%' ORDER BY LOWER(name)")
    fun getTerms(searchQuery: String, isChosenSelected: Boolean): Flow<List<TermDbEntity>>

    @Update
    suspend fun updateTerm(term: TermDbEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTerm(term: TermDbEntity): Long

    @Query("SELECT * FROM terms WHERE id IN(:randomTermIDs)")
    suspend fun getRandomTerms(randomTermIDs: List<Int>): List<TermDbEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTermSubject(termSubject: TermSubjectDbEntity)

    @Transaction
    @Query("SELECT * from terms WHERE id = :termId")
    fun getSubjectsOfTerm(termId: Long): Flow<SubjectsOfTermDb>


    @Transaction
    @Query("SELECT * from subjects WHERE name = :subjectName")
    fun getTermsOfSubject(subjectName: String): Flow<TermsOfSubjectDb>
}