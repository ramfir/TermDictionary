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
            termName.title.text = "Введите термин"
            termTranslation.title.text = "Введите английский перевод"
            termDefinition.title.text = "Введите определение"
            termNotes.title.text = "Введите ваши пометки"

            buttonAddTerm.setOnClickListener {
                val result = "${termName.data.text}\n" +
                        "${termTranslation.data.text}\n" +
                        "${autoCompleteTextViewSubject.text}\n" +
                        "${termDefinition.data.text}\n" +
                        "${termNotes.data.text}"
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