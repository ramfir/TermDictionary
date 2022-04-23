package com.firdavs.termdictionary.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.firdavs.termdictionary.domain.model.Subject

@Entity(
        tableName = "subjects",
        indices = [Index("name", unique = true)]
)
data class SubjectDBEntity(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val name: String
)

fun Subject.toData() = SubjectDBEntity(id, name)
fun List<Subject>.toData() = map { it.toData() }

fun SubjectDBEntity.toDomain() = Subject(id, name)
fun List<SubjectDBEntity>.toDomain() = map { it.toDomain() }
