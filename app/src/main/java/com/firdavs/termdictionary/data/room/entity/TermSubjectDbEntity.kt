package com.firdavs.termdictionary.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.firdavs.termdictionary.domain.model.TermSubject

@Entity(
        tableName = "terms_subjects",
        primaryKeys = ["termId", "subjectId"],
        indices = [Index("subjectId")],
        foreignKeys = [
            ForeignKey(
                    entity = TermDbEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["termId"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            ),
            ForeignKey(
                    entity = SubjectDBEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["subjectId"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            )]
)
data class TermSubjectDbEntity(
        val termId: Long,
        val subjectId: Long
)

fun TermSubject.toData() = TermSubjectDbEntity(termId, subjectId)
fun List<TermSubject>.toData() = map { it.toData() }

fun TermSubjectDbEntity.toDomain() = TermSubject(termId, subjectId)
fun List<TermSubjectDbEntity>.toDomain() = map { it.toDomain() }
