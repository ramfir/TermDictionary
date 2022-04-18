package com.firdavs.termdictionary.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.firdavs.termdictionary.domain.model.Major

@Entity(tableName = "majors")
data class MajorDbEntity(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val name: String
)

fun Major.toData() = MajorDbEntity(id, name)
fun List<Major>.toData() = map { it.toData() }

fun MajorDbEntity.toDomain() = Major(id, name)
fun List<MajorDbEntity>.toDomain() = map { it.toDomain() }
