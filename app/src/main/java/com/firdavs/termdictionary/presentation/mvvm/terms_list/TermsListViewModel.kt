package com.firdavs.termdictionary.presentation.mvvm.terms_list

import androidx.lifecycle.*
import com.firdavs.termdictionary.domain.model.Subject
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.domain.subjects.SubjectsInteractor
import com.firdavs.termdictionary.domain.terms.TermsInteractor
import com.firdavs.termdictionary.presentation.model.TermUI
import com.firdavs.termdictionary.presentation.model.toDomain
import com.firdavs.termdictionary.presentation.model.toUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TermsListViewModel(
        private val termsInteractor: TermsInteractor,
        private val subjectsInteractor: SubjectsInteractor,
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val isChosenSelected = MutableStateFlow(false)

    private val termsFlow = combine(
            searchQuery,
            isChosenSelected
    ) { query, isChosenSelected ->
        Pair(query, isChosenSelected)
    }.flatMapLatest { (query, isChosenSelected) ->
        termsInteractor.getTerms(query, isChosenSelected).map { it.toUI() }
    }

    val terms = termsFlow.asLiveData()

    private val termsEventChannel = Channel<TermEvent>()
    val termEvent = termsEventChannel.receiveAsFlow()

    val subjectFilter = MutableStateFlow("Введение в специальность")
    private val subjectFilterFlow: Flow<List<TermUI>> = combine(
            isChosenSelected,
            searchQuery,
            subjectFilter
    ) { isChosenSelected, query, subjectFilter ->
        Triple(isChosenSelected, query, subjectFilter)
    }.flatMapLatest { (isChosenSelected, query, subjectFilter) ->
        termsInteractor
            .getTermsOfSubject(subjectFilter)
            .map {
                it.terms
                    .filter {
                        (it.isChosen == isChosenSelected || it.isChosen) && it.name.contains(query,
                                                                                             true)
                    }
                    .toUI()
            }
    }

    val termsOfSubject = subjectFilterFlow.asLiveData()

    fun onTermClicked(term: TermUI, isChosenPropertyChanged: Boolean) {
        viewModelScope.launch {
            if (isChosenPropertyChanged) {
                termsInteractor.updateTerm(term.copy(isChosen = !term.isChosen).toDomain())
            } else {
                termsEventChannel.send(TermEvent.NavigateToTermDetailsFragment(term))
            }
        }
    }

    fun addNewTerms(newTerms: List<String>?) {
        viewModelScope.launch {
            if (newTerms == null)  {
                termsEventChannel.send(TermEvent.ShowMessage("Произошла ошибка"))
            } else {
                var subjectId: Long = 0
                newTerms.forEach {
                    val elements = it.split(";")
                    if (elements.size == 1 && elements[0].isNotBlank()) {
                        val subject = Subject(0, elements[0].trim())
                        subjectId = subjectsInteractor.insertSubject(subject)
                        println("mmm $subject $subjectId")
                    } else if (elements.size == 3) {
                        val term = Term(0,
                                        elements[0].trim(),
                                        elements[1].trim(),
                                        elements[2].trim(),
                                        "",
                                        false)
                        val termId = termsInteractor.insertTerm(term)
                        termsInteractor.insertTermSubjectConnection(termId, subjectId)
                    }
                }
                termsEventChannel.send(TermEvent.ShowMessage("Новые термины были импортированы"))
            }
        }
    }

    sealed class TermEvent {
        data class ShowMessage(val message: String): TermEvent()
        data class NavigateToTermDetailsFragment(val term: TermUI) : TermEvent()
    }
}