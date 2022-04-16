package com.firdavs.termdictionary.presentation.mvvm.terms_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.domain.terms.TermsInteractor
import kotlinx.coroutines.flow.Flow

class TermsListViewModel(private val termsInteractor: TermsInteractor): ViewModel() {

    val terms = termsInteractor.getTerms().asLiveData()

}