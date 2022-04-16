package com.firdavs.termdictionary.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.firdavs.termdictionary.data.room.entity.TermDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TermsDao {

    @Query("SELECT * FROM terms")
    fun getTerms(): Flow<List<TermDbEntity>>

    @Update
    suspend fun updateNotes(term: TermDbEntity)

    @Insert
    suspend fun insertTerm(term: TermDbEntity)
}