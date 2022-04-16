package com.firdavs.termdictionary.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.presentation.model.TermUI

@Entity(tableName = "terms")
data class TermDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val name: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val definition: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val translation: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val notes: String,
    val isChosen: Boolean
) {
    companion object {
        fun fromTerm(term: Term) =
            TermDbEntity(0, term.name, term.definition, term.translation, term.notes, term.isChosen)
    }
}

fun TermDbEntity.toDomain() = Term(name, definition, translation, notes, isChosen)
fun List<TermDbEntity>.toDomain() = map { it.toDomain() }
