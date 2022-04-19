package com.firdavs.termdictionary.presentation.mvvm.term_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.firdavs.termdictionary.domain.model.SubjectsOfTerm
import com.firdavs.termdictionary.domain.terms.TermsInteractor
import com.firdavs.termdictionary.presentation.model.TermUI
import com.firdavs.termdictionary.presentation.model.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TermDetailsViewModel(private val termsInteractor: TermsInteractor): ViewModel() {
    
    var termId = MutableStateFlow(1.toLong())

    private val termIdFlow = termId.flatMapLatest {
        termsInteractor.getSubjectsOfTerm(it)
    }
    val subjectsOfTerms: LiveData<SubjectsOfTerm> = termIdFlow.asLiveData()
    
    fun updateTerm(term: TermUI) {
        viewModelScope.launch {
            termsInteractor.updateTerm(term.toDomain())
        }
    }
}