package com.firdavs.termdictionary.presentation.mvvm.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firdavs.termdictionary.data.firestore.FirebaseService
import com.firdavs.termdictionary.data.model.UserData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val signUpEventChannel = Channel<SignUpEvent>()
    val signUpEvent = signUpEventChannel.receiveAsFlow()

    fun onLoginClicked(login: String, password: String) {
        viewModelScope.launch {
            val user: UserData? = FirebaseService.getUser(login, password)
            if (user != null) {
                signUpEventChannel.send(SignUpEvent.NavigateToTermsListFragment(user))
            } else {
                signUpEventChannel.send(SignUpEvent.WrongLoginOrPassword)
            }

        }
    }

    fun onLoginTeacherClicked(login: String, password: String) {

    }

    fun onSignUpClicked(login: String, password: String) {
        viewModelScope.launch {
            val user = UserData(login, password)
            if (FirebaseService.isAccountFree(user)) {
                FirebaseService.addUser(user)
                signUpEventChannel.send(SignUpEvent.NavigateToTermsListFragment(user))
            } else {
                signUpEventChannel.send(SignUpEvent.AccountAlreadyTaken)
            }
        }
    }

    sealed class SignUpEvent {
        object AccountAlreadyTaken: SignUpEvent()
        data class NavigateToTermsListFragment(val user: UserData): SignUpEvent()
        object WrongLoginOrPassword: SignUpEvent()
    }

}