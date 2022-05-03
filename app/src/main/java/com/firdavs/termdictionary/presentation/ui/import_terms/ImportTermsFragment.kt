package com.firdavs.termdictionary.presentation.ui.import_terms

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.databinding.FragmentImportTermsBinding
import com.firdavs.termdictionary.presentation.mvvm.import_terms.ImportTermsViewModel
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImportTermsFragment: Fragment(R.layout.fragment_import_terms) {

    private var _binding: FragmentImportTermsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ImportTermsViewModel by viewModel()

    private var userLogin = ""

    private val getContent: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { fileUri: Uri? ->
            fileUri?.let {
                val newTerms =
                    requireActivity().contentResolver
                        .openInputStream(it)
                        ?.bufferedReader()
                        ?.readLines()
                viewModel.importNewTerms(userLogin, newTerms)
                return@registerForActivityResult
            }
            Toast.makeText(requireContext(), "Файл не выбран", Toast.LENGTH_LONG).show()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentImportTermsBinding.bind(view)

        val sharedPref =
            requireActivity().getSharedPreferences(getString(R.string.preference_file_key),
                                                   Context.MODE_PRIVATE)
        userLogin = sharedPref.getString(getString(R.string.saved_login_key), "") ?: ""

        binding.uploadButton.setOnClickListener { getContent.launch("text/*") }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uploadEvent.collect { event ->
                when (event) {
                    is ImportTermsViewModel.UploadEvent.NavigateToTermsListFragment -> {
                        val action =
                            ImportTermsFragmentDirections.actionImportTermsFragmentToTermsListFragment(null)
                        findNavController().navigate(action)
                    }
                    is ImportTermsViewModel.UploadEvent.ShowMessage -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}