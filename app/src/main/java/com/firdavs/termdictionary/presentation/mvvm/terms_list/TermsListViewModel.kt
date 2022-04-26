package com.firdavs.termdictionary.presentation.mvvm.terms_list

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.firdavs.termdictionary.data.room.entity.TermDbEntity
import com.firdavs.termdictionary.domain.model.Subject
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.domain.subjects.SubjectsInteractor
import com.firdavs.termdictionary.domain.terms.TermsInteractor
import com.firdavs.termdictionary.presentation.model.TermUI
import com.firdavs.termdictionary.presentation.model.toDomain
import com.firdavs.termdictionary.presentation.model.toUI
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
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
        val firestoreDb = FirebaseFirestore.getInstance()

        val termsANDtranslations = context.assets
            .open("termsANDtranslations.txt")
            .bufferedReader()
            .readLines()
            .map { it.trim() }
        val terms = termsANDtranslations.filterIndexed { index, _ -> index % 2 == 0 }
        val translations = termsANDtranslations.filterIndexed { index, _ -> index % 2 == 1 }
        val definitions = context.assets.open("definitions.txt")
            .bufferedReader().readText().split("---").map { it.trim() }

        for (i in 0 until 92) {
            val newTerm = TermUI(0, terms[i], definitions[i], translations[i], "", false)

            firestoreDb.collection("Term").add(newTerm)
                .addOnSuccessListener {
                    Toast.makeText(context, "Term added", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                }
            val term = TermDbEntity(0, terms[i], definitions[i], translations[i], "", false)

        }
        loadTerms()
    }

    private fun loadTerms() {
        val firestoreDb = FirebaseFirestore.getInstance()

        firestoreDb.collection("Term").get()
            .addOnSuccessListener {
                for (documentSnapshot in it) {
                    val myTerm = documentSnapshot.toObject(TermUI::class.java)

                    //println("mmm $myTerm")
                }
                //println("mmm ${it.size()}")
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

    fun addNewTerms(newTerms: List<String>?) {
        viewModelScope.launch {
            if (newTerms == null) {
                termsEventChannel.send(TermEvent.ShowMessage("Произошла ошибка"))
            } else {
                var subjectId: Long = 0
                newTerms.forEach {
                    val elements = it.split(";")
                    if (elements.size == 1 && elements[0].isNotBlank()) {
                        val subject = Subject(0, elements[0].trim())
                        subjectId = subjectsInteractor.insertSubject(subject)
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
        data class ShowMessage(val message: String) : TermEvent()
        data class NavigateToTermDetailsFragment(val term: TermUI) : TermEvent()
    }
}