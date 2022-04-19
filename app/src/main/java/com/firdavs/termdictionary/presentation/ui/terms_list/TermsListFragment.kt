package com.firdavs.termdictionary.presentation.ui.terms_list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.databinding.FragmentTermsListBinding
import com.firdavs.termdictionary.presentation.model.toUI
import com.firdavs.termdictionary.presentation.mvvm.terms_list.TermsListViewModel
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class TermsListFragment : Fragment(R.layout.fragment_terms_list) {

    private var _binding: FragmentTermsListBinding? = null
    private val binding get() = _binding!!

    private var toggle: ActionBarDrawerToggle? = null

    private val viewModel: TermsListViewModel by viewModel()

    private val termsAdapter by lazy {
        AsyncListDifferDelegationAdapter(getTermsDiffCallback(),
                                         getTermsAdapterDelegate { term, changeChosenProperty ->
                                             viewModel.onTermClicked(term, changeChosenProperty)
                                         })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTermsListBinding.bind(view)

        // FIXME() toggle button disappears after closing filter dialog
        setHasOptionsMenu(true)
        initNavigationDrawer()

        binding.termsListRecyclerView.adapter = termsAdapter
        viewModel.terms.observe(viewLifecycleOwner) { termsAdapter.items = it.toList() }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.termEvent.collect { event ->
                when (event) {
                    is TermsListViewModel.TermEvent.NavigateToTermDetailsFragment -> {
                        val action =
                            TermsListFragmentDirections.actionTermsListFragmentToTermDetailFragment(
                                    event.term,
                                    event.term.name)
                        findNavController().navigate(action)
                    }
                }
            }
        }

        val major = arguments?.getString("major")
        val subject = arguments?.getString("subject")
        val isChosenSelected = arguments?.getBoolean("isChosenSelected") ?: false
        viewModel.isChosenSelected.value = isChosenSelected
        if (!subject.isNullOrEmpty()) {
            viewModel.subjectFilter.value = subject
        }

        viewModel.termsOfSubject.observe(viewLifecycleOwner) {
            if (subject != null) {
                termsAdapter.items = it.terms.toUI()
            } else {
                termsAdapter.items = viewModel.terms.value
            }
        }
    }
    
    private fun initNavigationDrawer() {
        toggle =
            ActionBarDrawerToggle(activity,
                                  binding.drawerLayout,
                                  R.string.open,
                                  R.string.close).also {
                binding.drawerLayout.addDrawerListener(it)
                it.syncState()
            }
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true) // for converting hamburger to back arrow

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.test -> {
                    val action = TermsListFragmentDirections.actionTermsListFragmentToTestFragment()
                    findNavController().navigate(action)
                }
                R.id.add_term -> {
                    val action =
                        TermsListFragmentDirections.actionTermsListFragmentToAddTermFragment()
                    findNavController().navigate(action)
                }
                R.id.extract_term -> Toast
                    .makeText(requireContext(), R.string.extract_terms, Toast.LENGTH_SHORT)
                    .show()
                R.id.settings -> Toast
                    .makeText(requireContext(), R.string.settings, Toast.LENGTH_SHORT)
                    .show()
                R.id.import_terms -> Toast
                    .makeText(requireContext(), R.string.import_terms, Toast.LENGTH_SHORT)
                    .show()
                R.id.contact -> Toast
                    .makeText(requireContext(), R.string.contact, Toast.LENGTH_SHORT)
                    .show()
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_terms_list, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.value = newText.orEmpty()
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle?.onOptionsItemSelected(item) == true) { return true }

        if (item.itemId == R.id.action_filter) {
            val action =
                TermsListFragmentDirections.actionTermsListFragmentToTermsListFilterFragment()
            findNavController().navigate(action)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}