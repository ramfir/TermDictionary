package com.firdavs.termdictionary.data.room.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.firdavs.termdictionary.domain.model.TermsOfSubject

data class TermsOfSubjectDb(
        @Embedded val subject: SubjectDBEntity,

        @Relation(
                parentColumn = "id",
                entityColumn = "id",
                associateBy = Junction(
                        value = TermSubjectDbEntity::class,
                        parentColumn = "subjectId",
                        entityColumn = "termId"
                )
        )
        val terms: List<TermDbEntity>
)

fun TermsOfSubjectDb.toDomain() = TermsOfSubject(subject.toDomain(), terms.toDomain())
fun List<TermsOfSubjectDb>.toDomain() = map { it.toDomain() }
