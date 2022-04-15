package com.example.termdictionary.presentation.ui.add_term

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.termdictionary.R
import com.example.termdictionary.databinding.FragmentAddTermBinding

class AddTermFragment: Fragment(R.layout.fragment_add_term) {

    private var _binding: FragmentAddTermBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddTermBinding.bind(view)

        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            termName.textInputLayoutTitle.hint = "Введите термин"
            termTranslation.textInputLayoutTitle.hint = "Введите английский перевод"
            termDefinition.textInputLayoutTitle.hint = "Введите определение"
            termNotes.textInputLayoutTitle.hint = "Введите ваши пометки"

            buttonAddTerm.setOnClickListener {
                val result = "${termName.textInputEditTextData.text}\n" +
                        "${termTranslation.textInputEditTextData.text}\n" +
                        "${autoCompleteTextViewSubject.text}\n" +
                        "${termDefinition.textInputEditTextData.text}\n" +
                        "${termNotes.textInputEditTextData.text}"
                Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setSubjectsAdapter()
    }

    private fun setSubjectsAdapter() {
        val subjects = resources.getStringArray(R.array.subjects)
        val subjectsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_filter_item, subjects)
        binding.autoCompleteTextViewSubject.setAdapter(subjectsAdapter)
    }
}