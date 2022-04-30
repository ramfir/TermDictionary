package com.firdavs.termdictionary.data.firestore

import android.util.Log
import com.firdavs.termdictionary.data.model.TermData
import com.firdavs.termdictionary.data.model.TermData.Companion.toTermData
import com.firdavs.termdictionary.data.model.UserData
import com.firdavs.termdictionary.data.model.UserData.Companion.toUserata
import com.firdavs.termdictionary.domain.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

object FirebaseService {

    fun addUser(user: UserData) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").add(user)
    }

    /*suspend fun getUser(user: UserData): UserData? {
        val db = FirebaseFirestore.getInstance()

    }*/

    suspend fun getTerm(id: String): TermData? {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("Term").document(id).get().await().toTermData()
        } catch (e: Exception) {
            Log.e("MyApp", "Error getting term", e)
            null
        }
    }

    suspend fun getTerms(): List<TermData> {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("Term").get().await().documents.mapNotNull { it.toTermData() }
        } catch (e: Exception) {
            Log.e("MyApp", "Error getting terms", e)
            emptyList()
        }
    }

    @ExperimentalCoroutinesApi
    fun getTermsFlow(): Flow<List<TermData>> {
        val db = FirebaseFirestore.getInstance()
        return callbackFlow {
            val listenerRegistration = db.collection("Terms")
                .addSnapshotListener {querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(message = "Error getching terms",
                        cause = firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    val map = querySnapshot!!.documents.mapNotNull { it.toTermData() }
                    offer(map)
                }
            awaitClose {
                Log.d("MyApp", "Cancelling terms listener")
                listenerRegistration.remove()
            }

        }
    }

    suspend fun isAccountFree(user: UserData): Boolean {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("Users")
        val users = userRef.get().await().documents.mapNotNull { it.toUserata() }
        return users.none { user.login == it.login }
    }

    suspend fun doesAccountExists(user: UserData): Boolean {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("Users")
        val users = userRef.get().await().documents.mapNotNull { it.toUserata() }
        return users.any { user == it }
    }

    suspend fun getUser(login: String, password: String): UserData? {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("Users")
        val users = userRef.get().await().documents.mapNotNull { it.toUserata() }
        return users.find { it.login == login && it.password == password }
    }

    suspend fun getTermIds(): List<String> {
        val db = FirebaseFirestore.getInstance()
        val result: List<String> = db.collection("Terms").get().await().documents.mapNotNull { it.id }
        return result
    }

    suspend fun addTermsForUsers(termIds: List<String>) {
        val db = FirebaseFirestore.getInstance()
        val userIds = db.collection("Users").get().await().documents.mapNotNull { it.id }
        userIds.forEach { userId ->
            println("mmm userId=$userId termIds=$termIds")
            //db.document("Users/$userId").update("terms", FieldValue.arrayUnion(termIds))
            termIds.forEach { termId ->
                db.document("Users/$userId").update("terms", FieldValue.arrayUnion(termId))
            }
        }
    }
}