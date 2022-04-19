package com.firdavs.termdictionary.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.firdavs.termdictionary.data.room.entity.TermsOfSubjectDb
import com.firdavs.termdictionary.data.room.entity.TermSubjectDbEntity
import com.firdavs.termdictionary.data.room.entity.SubjectsOfTerm

@Dao
interface TermSubjectDao {

    @Transaction
    @Query("SELECT * from subjects WHERE id = :subjectId")
    suspend fun getTermsInSubject(subjectId: Long): List<TermsOfSubjectDb>

    @Transaction
    @Query("SELECT * from terms WHERE id = :termId")
    suspend fun getSubjectsOfTerm(termId: Long): List<SubjectsOfTerm>

    @Insert
    fun insertTermSubject(termSubject: TermSubjectDbEntity)
}