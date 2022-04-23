package com.firdavs.termdictionary.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firdavs.termdictionary.data.room.entity.SubjectDBEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectsDao {

    @Query("SELECT * FROM subjects ORDER BY name")
    fun getSubjects(): Flow<List<SubjectDBEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubject(subject: SubjectDBEntity): Long

    @Query("SELECT id FROM subjects WHERE name = :subject")
    suspend fun getSubjectId(subject: String): Long
}