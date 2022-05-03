package com.firdavs.termdictionary.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.data.repository.SubjectsRepositoryImpl
import com.firdavs.termdictionary.data.repository.TermsRepositoryImpl
import com.firdavs.termdictionary.data.room.AppDatabase
import com.firdavs.termdictionary.domain.repository.SubjectsRepository
import com.firdavs.termdictionary.domain.repository.TermsRepository
import com.firdavs.termdictionary.domain.subjects.SubjectsInteractor
import com.firdavs.termdictionary.domain.terms.TermsInteractor
import com.firdavs.termdictionary.presentation.mvvm.filter_terms_list.TermsListFilterViewModel
import com.firdavs.termdictionary.presentation.mvvm.import_terms.ImportTermsViewModel
import com.firdavs.termdictionary.presentation.mvvm.login.LoginViewModel
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
    single<CoroutineScope> { CoroutineScope(SupervisorJob()) }
    single<TermsInteractor> { TermsInteractor(get()) }
    single<SubjectsInteractor> { SubjectsInteractor(get()) }
    viewModel { TermsListViewModel(get(), get(), androidContext()) }
    viewModel { TermDetailsViewModel(get()) }
    viewModel { TestFragmentViewModel(get()) }
    viewModel { TermsListFilterViewModel(get(), get()) }
    viewModel { LoginViewModel() }
    viewModel { ImportTermsViewModel(get(), get()) }
}

fun provideTermsRepositoryImpl(database: AppDatabase): TermsRepositoryImpl {
    return TermsRepositoryImpl(database.getTermsDao())
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
