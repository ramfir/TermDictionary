package com.firdavs.termdictionary.presentation.mvvm.filter_terms_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.firdavs.termdictionary.data.firestore.FirebaseService
import com.firdavs.termdictionary.data.model.UserData
import com.firdavs.termdictionary.domain.subjects.SubjectsInteractor
import com.firdavs.termdictionary.domain.terms.TermsInteractor
import com.firdavs.termdictionary.presentation.model.TermUI
import com.firdavs.termdictionary.presentation.model.toDomain
import com.firdavs.termdictionary.presentation.model.toFirestore
import kotlinx.coroutines.launch

class TermsListFilterViewModel(
        private val termsInteractor: TermsInteractor,
        private val subjectsInteractor: SubjectsInteractor,
) : ViewModel() {

    fun insertTerm(userLogin: String, subject: String, term: TermUI) {
        viewModelScope.launch {
            val termId: Long = termsInteractor.insertTerm(term.toDomain())
            val subjectId: Long = subjectsInteractor.getSubjectId(subject)
            termsInteractor.insertTermSubjectConnection(termId, subjectId)

            if (userLogin.isNotEmpty()) {
                FirebaseService.addTerm(term.toFirestore(listOf(subject)))
            }
        }
    }

    val subjects = subjectsInteractor.getSubjects().asLiveData()
}