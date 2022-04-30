package com.firdavs.termdictionary.presentation.mvvm.terms_list

import androidx.lifecycle.*
import com.firdavs.termdictionary.data.firestore.FirebaseService
import com.firdavs.termdictionary.data.model.TermFirestore
import kotlinx.coroutines.launch

class FirestoreTermsListViewModel: ViewModel() {

    private val _termData = MutableLiveData<TermFirestore>()
    val termData: LiveData<TermFirestore> = _termData

    /*private*/ val _terms = FirebaseService.getTermsFlow().asLiveData()
    //val terms: LiveData<List<TermData>> = _terms

    /*init {
        viewModelScope.launch {
            _terms.value = FirebaseService.getTermsFlow().asLiveData()
        }
    }*/

    fun getTermIds() {
        viewModelScope.launch {
            val termIds = FirebaseService.getTermIds()
            termIds.forEach {
                println("mmm $it")
            }
            addTermsForUsers(termIds)
        }
    }

    private suspend fun addTermsForUsers(termIds: List<String>) {
        FirebaseService.addTermsForUsers(termIds)
    }
}