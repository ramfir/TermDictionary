package com.firdavs.termdictionary.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.firdavs.termdictionary.data.room.dao.SubjectsDao
import com.firdavs.termdictionary.data.room.dao.TermsDao
import com.firdavs.termdictionary.data.room.entity.SubjectDBEntity
import com.firdavs.termdictionary.data.room.entity.TermDbEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

@Database(version = 1, entities = [TermDbEntity::class, SubjectDBEntity::class])
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
                addTerms(appDatabase.getTermsDao())
                addSubjects(appDatabase.getSubjectsDao())
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