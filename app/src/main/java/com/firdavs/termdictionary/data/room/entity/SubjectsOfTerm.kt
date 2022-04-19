package com.firdavs.termdictionary.data.room.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SubjectsOfTerm(
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