package com.firdavs.termdictionary.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.firdavs.termdictionary.data.room.entity.TermDbEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.io.File

@Database(
        version = 1,
        entities = [TermDbEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTermsDao(): TermsDao

    class Callback(
            private val context: Context,
            private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        val appDatabase: AppDatabase by inject(AppDatabase::class.java)

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = appDatabase.getTermsDao()
            applicationScope.launch {
                val termsANDtranslations = context.assets
                    .open("termsANDtranslations.txt")
                    .bufferedReader()
                    .readLines()
                    .map { it.trim() }
                val terms = mutableListOf<String>()
                var i = 0
                while (i < termsANDtranslations.size) {
                    terms.add(termsANDtranslations[i])
                    i += 2
                }
                val translations = mutableListOf<String>()
                i = 1
                while (i < termsANDtranslations.size) {
                    translations.add(termsANDtranslations[i])
                    i += 2
                }
                val definitions = context.assets.open("definitions.txt")
                    .bufferedReader().readText().split("---").map { it.trim() }
                for (i in 0 until 92) {
                    val term = TermDbEntity(
                            0,
                            terms[i],
                            definitions[i],
                            translations[i],
                            "",
                            false
                    )
                    dao.insertTerm(term)
                }
            }
        }
    }
}