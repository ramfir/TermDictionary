package com.firdavs.termdictionary.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TermUI(
    val name: String,
    val translation: String,
    val subject: String,
    val definition: String,
    val notes: String,
    val isChosen: Boolean
): Parcelable