package com.firdavs.termdictionary.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.presentation.model.TermUI

@Entity(
        tableName = "terms",
        indices = [Index("name", "definition", unique = true)]
)
data class TermDbEntity(
        @PrimaryKey(autoGenerate = true) val id: Long,
        @ColumnInfo(collate = ColumnInfo.NOCASE) val name: String,
        @ColumnInfo(collate = ColumnInfo.NOCASE) val definition: String,
        @ColumnInfo(collate = ColumnInfo.NOCASE) val translation: String,
        @ColumnInfo(collate = ColumnInfo.NOCASE) val notes: String,
        val isChosen: Boolean,
)

fun Term.toData() = TermDbEntity(id, name, definition, translation, notes, isChosen)
fun List<Term>.toData() = map { it.toData() }

fun TermDbEntity.toDomain() = Term(id, name, definition, translation, notes, isChosen)
fun List<TermDbEntity>.toDomain() = map { it.toDomain() }
