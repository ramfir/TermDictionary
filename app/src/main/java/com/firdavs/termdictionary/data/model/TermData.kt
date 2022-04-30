package com.firdavs.termdictionary.data.model

import android.os.Parcelable
import android.util.Log
import com.firdavs.termdictionary.presentation.model.TermUI
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize
import java.lang.Exception

@Parcelize
data class TermData(
        val id: String,
        val name: String,
        val definition: String,
        val translation: String,
        val notes: String,
        val isChosen: Boolean,
        val userIds: List<String>
): Parcelable {

    companion object {
        fun DocumentSnapshot.toTermData(): TermData? {
            try {
                val name = getString("name")!!
                val definition = getString("definition")!!
                val translation = getString("translation")!!
                val notes = getString("notes")!!
                val isChosen = getBoolean("chosen")!!
                val userIds = get("userIds") as List<String>
                return TermData(id, name, definition, translation, notes, isChosen, userIds)
            } catch (e: Exception) {
                Log.e("MyApp", "Error converting term", e)
                return null
            }
        }
    }

    fun toUI(): TermUI {
        return TermUI(1.toLong(), name, definition, translation, notes, isChosen, userIds)
    }
}

fun List<TermData>.toUI() = map { it.toUI() }
