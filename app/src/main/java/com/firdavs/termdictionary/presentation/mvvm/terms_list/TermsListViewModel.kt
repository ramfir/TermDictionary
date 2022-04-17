package com.firdavs.termdictionary.presentation.mvvm.terms_list

import androidx.lifecycle.*
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.domain.terms.TermsInteractor
import com.firdavs.termdictionary.presentation.model.TermUI
import com.firdavs.termdictionary.presentation.model.toDomain
import com.firdavs.termdictionary.presentation.model.toUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TermsListViewModel(private val termsInteractor: TermsInteractor): ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val termsFlow = searchQuery.flatMapLatest {
        termsInteractor.getTerms(it).map { it.toUI() }
    }

    val terms = termsFlow.asLiveData()

    private val taskEventChannel = Channel<TermEvent>()
    val termEvent = taskEventChannel.receiveAsFlow()

    fun onTermClicked(term: TermUI, isChosenPropertyChanged: Boolean) {
        viewModelScope.launch {
            if (isChosenPropertyChanged) {
                termsInteractor.updateTerm(term.copy(isChosen = !term.isChosen).toDomain())
            } else {
                taskEventChannel.send(TermEvent.NavigateToTermDetailsFragment(term))
            }
        }
    }

    sealed class TermEvent {
        data class NavigateToTermDetailsFragment(val term: TermUI): TermEvent()
    }
}