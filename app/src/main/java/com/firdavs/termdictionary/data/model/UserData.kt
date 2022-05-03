package com.firdavs.termdictionary.data.model

import android.os.Parcelable
import android.util.Log
import com.firdavs.termdictionary.presentation.model.UserUI
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(
    val login: String,
    val password: String
): Parcelable {

    companion object {
        fun DocumentSnapshot.toUserata(): UserData? {
            try {
                val login = getString("login")!!
                val password = getString("password")!!
                return UserData(login, password)
            } catch (e: Exception) {
                Log.e("MyApp", "Error converting user", e)
                return null
            }
        }
    }
}
