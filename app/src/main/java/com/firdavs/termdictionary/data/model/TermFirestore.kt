package com.firdavs.termdictionary.data.model

import android.os.Parcelable
import android.util.Log
import com.firdavs.termdictionary.presentation.model.TermUI
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize
import java.lang.Exception

@Parcelize
data class TermFirestore(
        val name: String,
        val definition: String,
        val translation: String,
        val subject: String
): Parcelable {

    companion object {
        fun DocumentSnapshot.toTermFirestore(): TermFirestore? {
            return try {
                val name = getString("name")!!
                val definition = getString("definition")!!
                val translation = getString("translation")!!
                val subject = getString("subject")!!
                TermFirestore(name, definition, translation, subject)
            } catch (e: Exception) {
                Log.e("MyApp", "Error converting term", e)
                null
            }
        }
    }

    fun toUI(): TermUI {
        return TermUI(0, name, definition, translation)
    }
}

fun List<TermFirestore>.toUI() = map { it.toUI() }
