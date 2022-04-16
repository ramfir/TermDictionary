package com.firdavs.termdictionary.presentation.ui.filter_terms_list

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.databinding.FragmentFilterTermsListBinding

class TermsListFilterFragment : DialogFragment(R.layout.fragment_filter_terms_list) {

    private var _binding: FragmentFilterTermsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFilterTermsListBinding.inflate(inflater, container, false)

        binding.buttonApplyFilter.setOnClickListener {
            val major = binding.autoCompleteTextViewMajor.text
            val subject = binding.autoCompleteTextViewSubject.text
            Toast.makeText(requireContext(), "$major\n$subject", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        }
    }

    override fun onResume() {
        super.onResume()

        setMajorsAdapter()
        setSubjectsAdapter()

    }

    private fun setMajorsAdapter() {
        val majors = resources.getStringArray(R.array.majors)
        val majorsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_filter_item, majors)
        binding.autoCompleteTextViewMajor.setAdapter(majorsAdapter)
    }

    private fun setSubjectsAdapter() {
        val subjects = resources.getStringArray(R.array.subjects)
        val subjectsAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_filter_item, subjects)
        binding.autoCompleteTextViewSubject.setAdapter(subjectsAdapter)
    }
}