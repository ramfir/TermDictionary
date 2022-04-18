package com.firdavs.termdictionary.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.firdavs.termdictionary.data.room.entity.MajorDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MajorsDao {

    @Query("SELECT * from majors")
    fun getMajors(): Flow<List<MajorDbEntity>>

    @Insert
    suspend fun insertMajor(major: MajorDbEntity)
}