package com.firdavs.termdictionary.presentation.model

import android.os.Parcelable
import com.firdavs.termdictionary.domain.model.Term
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TermUI(
        val id: Long,
        val name: String,
        val definition: String,
        val translation: String,
        val notes: String,
        val isChosen: Boolean,
) : Parcelable

fun Term.toUI() = TermUI(id, name, definition, translation, notes, isChosen)
fun List<Term>.toUI() = map { it.toUI() }

fun TermUI.toDomain() = Term(id, name, definition, translation, notes, isChosen)
fun List<TermUI>.toDomain() = map { it.toDomain() }