package com.firdavs.termdictionary.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.firdavs.termdictionary.data.room.entity.TermDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TermsDao {

    @Query("SELECT * FROM terms WHERE name LIKE '%' || :searchQuery || '%' ")
    fun getTerms(searchQuery: String): Flow<List<TermDbEntity>>

    @Update
    suspend fun updateTerm(term: TermDbEntity)

    @Insert
    suspend fun insertTerm(term: TermDbEntity)
}