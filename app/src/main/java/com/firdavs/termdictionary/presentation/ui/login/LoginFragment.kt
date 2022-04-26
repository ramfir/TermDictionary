package com.firdavs.termdictionary.presentation.ui.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.databinding.FragmentLoginBinding
import com.firdavs.termdictionary.presentation.mvvm.login.LoginViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.buttonSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }

        binding.buttonLogin.setOnClickListener {
            val login = binding.textInputEditTextLogin.text.toString()
            val password = binding.textInputEditTextPassword.text.toString()

            if (checkLoginPassword(login, password)) {
                viewModel.onLoginClicked(login, password)
            }
            hideKeyboard()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.signUpEvent.collect { event ->
                when (event) {
                    is LoginViewModel.SignUpEvent.WrongLoginOrPassword -> {
                        Toast
                            .makeText(requireContext(),
                                      "Неправильный логин или пароль",
                                      Toast.LENGTH_LONG)
                            .show()
                    }
                    is LoginViewModel.SignUpEvent.NavigateToTermsListFragment -> {
                        val login = binding.textInputEditTextLogin.text.toString()
                        Toast
                            .makeText(requireContext(), "Вы вошли как $login", Toast.LENGTH_LONG)
                            .show()
                        val action = LoginFragmentDirections.actionLoginFragmentToTermsListFragment(
                            null,
                            user = event.user)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun checkLoginPassword(login: String, password: String): Boolean {
        if (login.isBlank() || password.isBlank()) {
            if (login.isBlank()) binding.textInputLayoutLogin.setErrorMessage("Логин не может быть пустым")
            if (password.isBlank()) binding.textInputLayoutPassword.setErrorMessage("Пароль не может быть пустым")
            return false
        }
        return true
    }

    private fun TextInputLayout.setErrorMessage(errorText: String? = null) {
        error = errorText
        isErrorEnabled = errorText != null
    }

    private fun hideKeyboard() {
        val imm = ContextCompat.getSystemService(requireContext(),
                                                 InputMethodManager::class.java) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}