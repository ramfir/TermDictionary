package com.firdavs.termdictionary.presentation.mvvm.term_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firdavs.termdictionary.domain.terms.TermsInteractor
import com.firdavs.termdictionary.presentation.model.TermUI
import com.firdavs.termdictionary.presentation.model.toDomain
import kotlinx.coroutines.launch

class TermDetailsViewModel(private val termsInteractor: TermsInteractor): ViewModel() {

    fun updateTerm(term: TermUI) {
        viewModelScope.launch {
            termsInteractor.updateTerm(term.toDomain())
        }
    }
}