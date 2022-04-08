package com.example.termdictionary.presentation.ui.filter_terms_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.termdictionary.R
import com.example.termdictionary.databinding.FragmentFilterTermsListBinding

class TermsListFilterFragment: DialogFragment(R.layout.fragment_filter_terms_list) {

    private var _binding: FragmentFilterTermsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFilterTermsListBinding.inflate(inflater, container, false)

        binding.buttonApplyFilter.setOnClickListener{
            val major = binding.autoCompleteTextViewMajor.text
            val semester = binding.autoCompleteTextViewSemester.text
            val subject = binding.autoCompleteTextViewSubject.text
            Toast.makeText(requireContext(), "$major\n$semester\n$subject", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        setMajorsAdapter()
        setSemestersAdapter()
        setSubjectsAdapter()

    }

    private fun setMajorsAdapter() {
        val majors = resources.getStringArray(R.array.majors)
        val majorsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_filter_item, majors)
        binding.autoCompleteTextViewMajor.setAdapter(majorsAdapter)
    }
    private fun setSemestersAdapter() {
        val semesters = resources.getStringArray(R.array.semesters)
        val semestersAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_filter_item, semesters)
        binding.autoCompleteTextViewSemester.setAdapter(semestersAdapter)
    }
    private fun setSubjectsAdapter() {
        val subjects = resources.getStringArray(R.array.subjects)
        val subjectsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_filter_item, subjects)
        binding.autoCompleteTextViewSubject.setAdapter(subjectsAdapter)
    }
}