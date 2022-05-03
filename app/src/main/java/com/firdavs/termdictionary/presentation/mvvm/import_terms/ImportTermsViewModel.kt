package com.firdavs.termdictionary.presentation.mvvm.import_terms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firdavs.termdictionary.data.firestore.FirebaseService
import com.firdavs.termdictionary.domain.model.Subject
import com.firdavs.termdictionary.domain.subjects.SubjectsInteractor
import com.firdavs.termdictionary.domain.terms.TermsInteractor
import com.firdavs.termdictionary.presentation.model.TermUI
import com.firdavs.termdictionary.presentation.model.toDomain
import com.firdavs.termdictionary.presentation.model.toFirestore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ImportTermsViewModel(
    private val termsInteractor: TermsInteractor,
    private val subjectsInteractor: SubjectsInteractor
): ViewModel() {

    private val uploadEventChannel = Channel<UploadEvent>()
    val uploadEvent = uploadEventChannel.receiveAsFlow()

    fun importNewTerms(userLogin: String, newTerms: List<String>?) {
        viewModelScope.launch {
            if (newTerms == null) {
                uploadEventChannel.send(UploadEvent.ShowMessage("Произошла ошибка"))
            } else {
                var subjects = listOf<String>()
                val subjectIds = mutableListOf<Long>()
                var subjectId: Long = 0
                newTerms.forEach {
                    if (it.contains("|")) {
                        subjectIds.clear()
                        subjects = it.split("|").filter { it.isNotBlank() }.map { it.trim() }
                        subjects.forEach { subject ->
                            subjectId = subjectsInteractor.insertSubject(Subject(0, subject))
                            if (subjectId == (-1).toLong()) {
                                subjectId = subjectsInteractor.getSubjectId(subject)
                            }
                            subjectIds.add(subjectId)
                        }
                    } else if (it.contains(";")) {
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
                uploadEventChannel.send(UploadEvent.ShowMessage("Новые термины были импортированы"))
                uploadEventChannel.send(UploadEvent.NavigateToTermsListFragment)
            }
        }
    }

    sealed class UploadEvent {
        data class ShowMessage(val message: String) : UploadEvent()
        object NavigateToTermsListFragment: UploadEvent()
    }
}