package com.example.termdictionary.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TermUI(
    val name: String
): Parcelable