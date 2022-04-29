package com.firdavs.termdictionary.presentation.mvvm.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.domain.terms.TermsInteractor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class TestFragmentViewModel(private val termsInteractor: TermsInteractor): ViewModel() {

    val terms = MutableLiveData<List<Term>>()
    val answer = MutableLiveData<Term>()

    private val testEventChannel = Channel<TestEvent>()
    val testEvent = testEventChannel.receiveAsFlow()

    var currentQuestion = 1
    var errors = 0

    init {
        getRandomTerms()
    }

    private fun getRandomTerms() {
        viewModelScope.launch {
            val randomTermIDs = List(4) { Random.nextInt(1, 92)}
            val randomTerms = termsInteractor.getRandomTerms(randomTermIDs)
            if (randomTerms.size != 4) {
                getRandomTerms()
                return@launch
            }
            terms.value = randomTerms
            answer.value = randomTerms.random()
        }
    }

    fun onButtonClicked(buttonText: String, userAnswer: String) {
        viewModelScope.launch {
            if (buttonText == "Ответить") {
                checkAnswer(userAnswer)
                testEventChannel.send(TestEvent.ChangeButton("Далее", R.color.white, R.color.red_500))
            } else if (buttonText == "Далее") {
                currentQuestion++
                if (currentQuestion <= 5) {
                    getRandomTerms()
                    testEventChannel.send(TestEvent.ChangeButton("Ответить", R.color.red_500, R.color.white))
                } else {
                    testEventChannel.send(TestEvent.ShowResult(errors))
                    testEventChannel.send(TestEvent.HideElements)
                }
            } else {
                restart()
            }
        }

    }

    private suspend fun checkAnswer(userAnswer: String) {
        if (userAnswer == answer.value?.name) {
            testEventChannel.send(TestEvent.ShowRightAnswerToast)
        } else {
            errors++
            testEventChannel.send(TestEvent.ShowWrongAnswerToast(answer.value?.name.orEmpty()))
        }
    }

    private suspend fun restart() {
        errors = 0
        currentQuestion = 1
        testEventChannel.send(TestEvent.ShowElements)
        testEventChannel.send(TestEvent.ChangeButton("Ответить", R.color.red_500, R.color.white))
        getRandomTerms()
    }

    sealed class TestEvent {
        object ShowRightAnswerToast: TestEvent()
        data class ShowWrongAnswerToast(val answer: String): TestEvent()
        data class ChangeButton(val text: String, val textColor: Int, val buttonColor: Int): TestEvent()
        data class ShowResult(val errors: Int): TestEvent()
        object ShowElements: TestEvent()
        object HideElements: TestEvent()
    }
}