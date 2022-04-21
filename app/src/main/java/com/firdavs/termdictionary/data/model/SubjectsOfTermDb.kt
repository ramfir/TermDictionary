package com.firdavs.termdictionary.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.firdavs.termdictionary.data.room.entity.SubjectDBEntity
import com.firdavs.termdictionary.data.room.entity.TermDbEntity
import com.firdavs.termdictionary.data.room.entity.TermSubjectDbEntity
import com.firdavs.termdictionary.data.room.entity.toDomain
import com.firdavs.termdictionary.domain.model.SubjectsOfTerm

data class SubjectsOfTermDb(
        @Embedded val term: TermDbEntity,

        @Relation(
                parentColumn = "id",
                entityColumn = "id",
                associateBy = Junction(
                        value = TermSubjectDbEntity::class,
                        parentColumn = "termId",
                        entityColumn = "subjectId"
                )
        )
        val subjects: List<SubjectDBEntity>
)

fun SubjectsOfTermDb.toDomain() = SubjectsOfTerm(term.toDomain(), subjects.toDomain())
fun List<SubjectsOfTermDb>.toDomain() = map { it.toDomain() }