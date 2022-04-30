package com.firdavs.termdictionary.data.room

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.firdavs.termdictionary.data.firestore.FirebaseService
import com.firdavs.termdictionary.data.room.dao.SubjectsDao
import com.firdavs.termdictionary.data.room.dao.TermsDao
import com.firdavs.termdictionary.data.room.entity.SubjectDBEntity
import com.firdavs.termdictionary.data.room.entity.TermDbEntity
import com.firdavs.termdictionary.data.room.entity.TermSubjectDbEntity
import com.firdavs.termdictionary.domain.model.Subject
import com.firdavs.termdictionary.domain.model.Term
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

@Database(version = 1,
          entities = [TermDbEntity::class, SubjectDBEntity::class, TermSubjectDbEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTermsDao(): TermsDao
    abstract fun getSubjectsDao(): SubjectsDao

    class Callback(
        private val context: Context,
        private val applicationScope: CoroutineScope,
    ) : RoomDatabase.Callback() {

        val appDatabase: AppDatabase by inject(AppDatabase::class.java)

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            applicationScope.launch {
                try {
                    addTermsFromFirestoreToRoom()
                    //addTerms(appDatabase.getTermsDao())
                    //addSubjects(appDatabase.getSubjectsDao())
                    //addTermSubject(appDatabase.getTermsDao())
                } catch (e: SQLiteConstraintException) {
                    Log.d("MyApp", "${e.message}")
                }
            }
        }

        private suspend fun addTermsFromFirestoreToRoom() {
            val termsFromFirestore = FirebaseService.getTerms()
            termsFromFirestore.forEach { termFirestore ->
                var subjectId = appDatabase.getSubjectsDao().insertSubject(SubjectDBEntity(0, termFirestore.subject))
                if (subjectId == (-1).toLong()) {
                    subjectId = appDatabase.getSubjectsDao().getSubjectId(termFirestore.subject)
                }
                //println("mmm addTermsFromFirestoreToRoom subjectId=$subjectId")
                var termId = appDatabase.getTermsDao().insertTerm(TermDbEntity(0,
                                                                               termFirestore.name,
                                                                               termFirestore.definition,
                                                                               termFirestore.translation,
                                                                               "",
                                                                               false))
                if (termId == (-1).toLong()) {
                    termId = appDatabase.getTermsDao().getTermId(termFirestore.name, termFirestore.definition)
                }
                appDatabase.getTermsDao().insertTermSubject(TermSubjectDbEntity(termId, subjectId))
            }
        }

        private suspend fun addTermSubject(dao: TermsDao) {
            for (i in 1..46) {
                try {
                    val termSubject = TermSubjectDbEntity(i.toLong(), 1)
                    dao.insertTermSubject(termSubject)
                } catch (e: SQLiteConstraintException) {
                    Log.d("MyApp", "$i --> ${e.message}")
                }
            }
            for (i in 47..92) {
                try {
                    val termSubject1 = TermSubjectDbEntity(i.toLong(), 1)
                    val termSubject2 = TermSubjectDbEntity(i.toLong(), 2)
                    dao.insertTermSubject(termSubject1)
                    dao.insertTermSubject(termSubject2)
                } catch (e: SQLiteConstraintException) {
                    Log.d("MyApp", "$i --> ${e.message}")
                }

            }
        }

        private suspend fun addSubjects(dao: SubjectsDao) {
            val subjects =
                context.assets.open("subjects.txt").bufferedReader().readLines().map { it.trim() }
            subjects.forEach {
                val subject = SubjectDBEntity(0, it)
                dao.insertSubject(subject)
            }
        }

        private suspend fun addTerms(dao: TermsDao) {
            val termsANDtranslations = context.assets
                .open("termsANDtranslations.txt")
                .bufferedReader()
                .readLines()
                .map { it.trim() }
            val terms = termsANDtranslations.filterIndexed { index, _ -> index % 2 == 0 }
            val translations = termsANDtranslations.filterIndexed { index, _ -> index % 2 == 1 }
            val definitions = context.assets.open("definitions.txt")
                .bufferedReader().readText().split("---").map { it.trim() }
            for (i in 0 until 92) {
                val term = TermDbEntity(0, terms[i], definitions[i], translations[i], "", false)
                dao.insertTerm(term)
            }
        }
    }
}