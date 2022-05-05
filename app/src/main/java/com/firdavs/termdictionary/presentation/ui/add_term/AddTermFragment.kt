package com.firdavs.termdictionary.presentation.ui.add_term

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.data.model.UserData
import com.firdavs.termdictionary.databinding.FragmentAddTermBinding
import com.firdavs.termdictionary.presentation.model.TermUI
import com.firdavs.termdictionary.presentation.mvvm.filter_terms_list.TermsListFilterViewModel
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddTermFragment : Fragment(R.layout.fragment_add_term) {

    private var _binding: FragmentAddTermBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TermsListFilterViewModel by viewModel()

    private var userLogin = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddTermBinding.bind(view)

        val sharedPref =
            requireActivity().getSharedPreferences(getString(R.string.preference_file_key),
                                                   Context.MODE_PRIVATE)
        userLogin = sharedPref.getString(getString(R.string.saved_login_key), "") ?: ""

        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            termName.textInputLayoutTitle.hint = getString(R.string.term)
            termTranslation.textInputLayoutTitle.hint = getString(R.string.translation)
            termDefinition.textInputLayoutTitle.hint = getString(R.string.definition)
            termNotes.textInputLayoutTitle.hint = getString(R.string.your_notes)

            termName.textInputEditTextData.addTextChangedListener {
                termName.textInputLayoutTitle.setErrorMessage(
                    if (it.isNullOrBlank()) "Название термина не может быть пустым" else null
                )
            }
            termTranslation.textInputEditTextData.addTextChangedListener {
                termTranslation.textInputLayoutTitle.setErrorMessage(
                    if (it.isNullOrBlank()) "Перевод не может быть пустым" else null
                )
            }
            termDefinition.textInputEditTextData.addTextChangedListener {
                termDefinition.textInputLayoutTitle.setErrorMessage(
                    if (it.isNullOrBlank()) "Определение не может быть пустым" else null
                )
            }

            viewModel.subjects.observe(viewLifecycleOwner) {
                binding.autoCompleteTextViewSubject.setAdapter(
                    ArrayAdapter(requireContext(),
                                 R.layout.dropdown_filter_item,
                                 it.map { it.name })
                )
            }

            buttonAddTerm.setOnClickListener {
                hideKeyboard()
                addTerm(userLogin,
                        termName.textInputEditTextData.text.toString(),
                        termTranslation.textInputEditTextData.text.toString(),
                        autoCompleteTextViewSubject.text.toString(),
                        termDefinition.textInputEditTextData.text.toString(),
                        termNotes.textInputEditTextData.text.toString()
                )
            }
        }
    }

    private fun addTerm(
        userLogin: String,
        name: String,
        translation: String,
        subject: String,
        definition: String,
        notes: String,
    ) {
        if (name.isBlank() || translation.isBlank() || subject.isBlank() || definition.isBlank()) {
            if (name.isBlank()) binding.termName.textInputLayoutTitle.setErrorMessage("Название термина не может быть пустым")
            if (translation.isBlank()) binding.termTranslation.textInputLayoutTitle.setErrorMessage(
                "Перевод не может быть пустым")
            if (definition.isBlank()) binding.termDefinition.textInputLayoutTitle.setErrorMessage("Определение не может быть пустым")
            if (subject.isBlank()) binding.textInputLayoutSubjects.setErrorMessage("Выберите дисциплину")
            return
        }
        viewModel.insertTerm(userLogin, subject, TermUI(0, name, definition, translation, notes, false))
        Toast.makeText(requireContext(), "Термин добавлен", Toast.LENGTH_SHORT).show()
        clearEditTexts()
    }

    private fun clearEditTexts() {
        with(binding) {
            termName.textInputEditTextData.text?.clear()
            termName.textInputLayoutTitle.setErrorMessage(null)
            termTranslation.textInputEditTextData.text?.clear()
            termTranslation.textInputLayoutTitle.setErrorMessage(null)
            autoCompleteTextViewSubject.text?.clear()
            textInputLayoutSubjects.setErrorMessage(null)
            termDefinition.textInputEditTextData.text?.clear()
            termDefinition.textInputLayoutTitle.setErrorMessage(null)
            termNotes.textInputEditTextData.text?.clear()
            termNotes.textInputLayoutTitle.setErrorMessage(null)
            buttonAddTerm.requestFocusFromTouch()
        }
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