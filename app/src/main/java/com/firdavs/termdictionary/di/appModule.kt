package com.firdavs.termdictionary.di

import android.content.Context
import androidx.room.Room
import com.firdavs.termdictionary.data.repository.MajorsRepositoryImpl
import com.firdavs.termdictionary.data.repository.SubjectsRepositoryImpl
import com.firdavs.termdictionary.data.repository.TermsRepositoryImpl
import com.firdavs.termdictionary.data.room.AppDatabase
import com.firdavs.termdictionary.domain.majors.MajorsInteractor
import com.firdavs.termdictionary.domain.repository.MajorsRepository
import com.firdavs.termdictionary.domain.repository.SubjectsRepository
import com.firdavs.termdictionary.domain.repository.TermsRepository
import com.firdavs.termdictionary.domain.subjects.SubjectsInteractor
import com.firdavs.termdictionary.domain.terms.TermsInteractor
import com.firdavs.termdictionary.presentation.mvvm.filter_terms_list.TermsListFilterViewModel
import com.firdavs.termdictionary.presentation.mvvm.term_details.TermDetailsViewModel
import com.firdavs.termdictionary.presentation.mvvm.terms_list.TermsListViewModel
import com.firdavs.termdictionary.presentation.mvvm.test.TestFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AppDatabase> { provideAppDatabase(androidContext(), get()) }
    single<TermsRepository> { provideTermsRepositoryImpl(get())}
    single<SubjectsRepository> { provideSubjectsRepositoryImpl(get())}
    single<MajorsRepository> { provideMajorsRepositoryImpl(get())}
    single<CoroutineScope> { CoroutineScope(SupervisorJob()) }
    single<TermsInteractor> { TermsInteractor(get()) }
    single<SubjectsInteractor> { SubjectsInteractor(get()) }
    single<MajorsInteractor> { MajorsInteractor(get()) }
    viewModel { TermsListViewModel(get(), get()) }
    viewModel { TermDetailsViewModel(get()) }
    viewModel { TestFragmentViewModel(get()) }
    viewModel { TermsListFilterViewModel(get(), get(), get()) }
}

fun provideMajorsRepositoryImpl(database: AppDatabase): MajorsRepositoryImpl {
    return MajorsRepositoryImpl(database.getMajorsDao())
}

fun provideTermsRepositoryImpl(database: AppDatabase): TermsRepositoryImpl {
    return TermsRepositoryImpl(database.getTermsDao(), database.getTermSubjectDao())
}

fun provideSubjectsRepositoryImpl(database: AppDatabase): SubjectsRepositoryImpl {
    return SubjectsRepositoryImpl(database.getSubjectsDao())
}

fun provideAppDatabase(context: Context, coroutineScope: CoroutineScope): AppDatabase {
    return Room
        .databaseBuilder(context, AppDatabase::class.java, "termDictionaryDatabase.db")
        .fallbackToDestructiveMigration()
        .addCallback(AppDatabase.Callback(context, coroutineScope))
        .build()
}
