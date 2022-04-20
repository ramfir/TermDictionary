package com.firdavs.termdictionary.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.firdavs.termdictionary.data.room.dao.MajorsDao
import com.firdavs.termdictionary.data.room.dao.SubjectsDao
import com.firdavs.termdictionary.data.room.dao.TermSubjectDao
import com.firdavs.termdictionary.data.room.dao.TermsDao
import com.firdavs.termdictionary.data.room.entity.MajorDbEntity
import com.firdavs.termdictionary.data.room.entity.SubjectDBEntity
import com.firdavs.termdictionary.data.room.entity.TermDbEntity
import com.firdavs.termdictionary.data.room.entity.TermSubjectDbEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

@Database(version = 1,
          entities = [TermDbEntity::class, SubjectDBEntity::class, MajorDbEntity::class, TermSubjectDbEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTermsDao(): TermsDao
    abstract fun getSubjectsDao(): SubjectsDao
    abstract fun getMajorsDao(): MajorsDao
    abstract fun getTermSubjectDao(): TermSubjectDao

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
                addMajors(appDatabase.getMajorsDao())
                addTermSubject(appDatabase.getTermSubjectDao())
            }
        }

        private suspend fun addTermSubject(dao: TermSubjectDao) {
            for (i in 1 .. 46) {
                val termSubject = TermSubjectDbEntity(i.toLong(), 1)
                dao.insertTermSubject(termSubject)
            }
            for (i in 47 .. 92) {
                val termSubject1 = TermSubjectDbEntity(i.toLong(), 1)
                val termSubject2 = TermSubjectDbEntity(i.toLong(), 2)
                dao.insertTermSubject(termSubject1)
                dao.insertTermSubject(termSubject2)
            }
        }

        private suspend fun addMajors(dao: MajorsDao) {
            val majors =
                context.assets.open("majors.txt").bufferedReader().readLines().map { it.trim() }
            majors.forEach {
                val major = MajorDbEntity(0, it)
                dao.insertMajor(major)
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