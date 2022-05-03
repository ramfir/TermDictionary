package com.firdavs.termdictionary.presentation.mvvm.terms_list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.firdavs.termdictionary.data.firestore.FirebaseService
import com.firdavs.termdictionary.data.model.TermFirestore
import com.firdavs.termdictionary.domain.model.Subject
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.domain.subjects.SubjectsInteractor
import com.firdavs.termdictionary.domain.terms.TermsInteractor
import com.firdavs.termdictionary.presentation.model.TermUI
import com.firdavs.termdictionary.presentation.model.toDomain
import com.firdavs.termdictionary.presentation.model.toFirestore
import com.firdavs.termdictionary.presentation.model.toUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TermsListViewModel(
    private val termsInteractor: TermsInteractor,
    private val subjectsInteractor: SubjectsInteractor,
    private val context: Context,
) : ViewModel() {

    init {
        //addTermsToFirestore()
    }

    private fun addTermsToFirestore() {
        val termsANDtranslations = context.assets
            .open("termsANDtranslations.txt")
            .bufferedReader()
            .readLines()
            .map { it.trim() }
        val terms = termsANDtranslations.filterIndexed { index, _ -> index % 2 == 0 }
        val translations = termsANDtranslations.filterIndexed { index, _ -> index % 2 == 1 }
        val definitions = context.assets.open("definitions.txt")
            .bufferedReader().readText().split("---").map { it.trim() }

        for (i in 0 until 46) {
            val newTerm = TermUI(0, terms[i], definitions[i], translations[i], "", false)
            viewModelScope.launch {
                FirebaseService.addTerm(newTerm.toFirestore(listOf("Введение в специальность",
                                                                   "Проектирование человеко-машинного взаимодействия")))
            }
        }
        for (i in 46 until 92) {
            val newTerm = TermUI(0, terms[i], definitions[i], translations[i], "", false)
            viewModelScope.launch {
                FirebaseService.addTerm(newTerm.toFirestore(listOf("Проектирование человеко-машинного взаимодействия")))
            }
        }
    }

    fun addTermsFromFirestore(newTerms: List<TermFirestore>) {
        viewModelScope.launch {
            newTerms.forEach { termFirestore ->
                val subjectIds = mutableListOf<Long>()
                termFirestore.subjects.forEach { subject ->
                    var subjectId = subjectsInteractor.insertSubject(Subject(0, subject))
                    if (subjectId == (-1).toLong()) {
                        subjectId = subjectsInteractor.getSubjectId(subject)
                    }
                    subjectIds.add(subjectId)
                }

                var termId = termsInteractor.insertTerm(Term(0,
                                                             termFirestore.name,
                                                             termFirestore.definition,
                                                             termFirestore.translation,
                                                             "",
                                                             false))
                if (termId == (-1).toLong()) {
                    termId = termsInteractor.getTermId(termFirestore.name, termFirestore.definition)
                }
                subjectIds.forEach { subjectId ->
                    termsInteractor.insertTermSubjectConnection(termId, subjectId)
                }
            }
        }
    }

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

    fun importNewTerms(userLogin: String, newTerms: List<String>?) {
        viewModelScope.launch {
            if (newTerms == null) {
                termsEventChannel.send(TermEvent.ShowMessage("Произошла ошибка"))
            } else {
                var subjects = listOf<String>()
                val subjectIds = mutableListOf<Long>()
                var subjectId: Long = 0
                newTerms.forEach {
                    if (it.contains("|")/* elements.size == 1 && elements[0].isNotBlank()*/) {
                        subjectIds.clear()
                        subjects = it.split("|").filter { it.isNotBlank() }.map { it.trim() }
                        subjects.forEach { subject ->
                            subjectId = subjectsInteractor.insertSubject(Subject(0, subject))
                            if (subjectId == (-1).toLong()) {
                                subjectId = subjectsInteractor.getSubjectId(subject)
                            }
                            subjectIds.add(subjectId)
                        }
                    } else if (it.contains(";")/*elements.size == 3*/) {
                        val elements = it.split(";")
                        val term = TermUI(0,
                                        elements[0].trim(),
                                        elements[1].trim(),
                                        elements[2].trim(),
                                        "",
                                        false)
                        var termId = termsInteractor.insertTerm(term.toDomain())
                        if (termId == (-1).toLong()) {
                            termId = termsInteractor.getTermId(term.name, term.definition)
                        }
                        subjectIds.forEach { subjectId ->
                            termsInteractor.insertTermSubjectConnection(termId, subjectId)
                        }

                        if (userLogin.isNotEmpty()) {
                            FirebaseService.addTerm(term.toFirestore(subjects))
                        }
                    }
                }
                termsEventChannel.send(TermEvent.ShowMessage("Новые термины были импортированы"))
            }
        }
    }

    sealed class TermEvent {
        data class ShowMessage(val message: String) : TermEvent()
        data class NavigateToTermDetailsFragment(val term: TermUI) : TermEvent()
    }
}