package com.firdavs.termdictionary.presentation.mvvm.terms_list

import androidx.lifecycle.*
import com.firdavs.termdictionary.data.firestore.FirebaseService
import com.firdavs.termdictionary.data.model.TermData
import com.firdavs.termdictionary.data.model.toUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FirestoreTermsListViewModel: ViewModel() {

    private val _termData = MutableLiveData<TermData>()
    val termData: LiveData<TermData> = _termData

    /*private*/ val _terms = FirebaseService.getTermsFlow().asLiveData()
    //val terms: LiveData<List<TermData>> = _terms

    /*init {
        viewModelScope.launch {
            _terms.value = FirebaseService.getTermsFlow().asLiveData()
        }
    }*/
}