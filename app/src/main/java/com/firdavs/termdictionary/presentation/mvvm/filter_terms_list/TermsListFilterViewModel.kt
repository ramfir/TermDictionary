package com.firdavs.termdictionary.presentation.mvvm.filter_terms_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.firdavs.termdictionary.domain.majors.MajorsInteractor
import com.firdavs.termdictionary.domain.subjects.SubjectsInteractor

class TermsListFilterViewModel(
        private val subjectsInteractor: SubjectsInteractor,
        private val majorsInteractor: MajorsInteractor,
) : ViewModel() {

    val subjects = subjectsInteractor.getSubjects().asLiveData()
    val majors = majorsInteractor.getMajors().asLiveData()
}