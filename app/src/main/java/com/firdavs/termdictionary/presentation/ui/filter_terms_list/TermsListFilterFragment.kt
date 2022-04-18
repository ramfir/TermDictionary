package com.firdavs.termdictionary.presentation.ui.filter_terms_list

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.databinding.FragmentFilterTermsListBinding
import com.firdavs.termdictionary.presentation.mvvm.filter_terms_list.TermsListFilterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TermsListFilterFragment : DialogFragment(R.layout.fragment_filter_terms_list) {

    private var _binding: FragmentFilterTermsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TermsListFilterViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentFilterTermsListBinding.inflate(inflater, container, false)

        binding.buttonApplyFilter.setOnClickListener {
            val major = binding.autoCompleteTextViewMajor.text
            val subject = binding.autoCompleteTextViewSubject.text
            val isChosenSelected = binding.chosenSwitch.isChecked
            val action =
                TermsListFilterFragmentDirections
                    .actionTermsListFilterFragmentToTermsListFragment2(isChosenSelected = isChosenSelected)
            findNavController().navigate(action)
            dismiss()
        }
        dialog?.setCanceledOnTouchOutside(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.subjects.observe(viewLifecycleOwner) {
            binding.autoCompleteTextViewSubject.setAdapter(
                    ArrayAdapter(requireContext(), R.layout.dropdown_filter_item, it.map { it.name })
            )
        }
        viewModel.majors.observe(viewLifecycleOwner) {
            binding.autoCompleteTextViewMajor.setAdapter(
                    ArrayAdapter(requireContext(), R.layout.dropdown_filter_item, it.map { it.name })
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        }
    }
}