package com.example.termdictionary.presentation.ui.terms_list

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
import androidx.navigation.fragment.findNavController
import com.example.termdictionary.R
import com.example.termdictionary.databinding.FragmentTermsListBinding
import com.example.termdictionary.presentation.model.TermUI

class TermsListFragment : Fragment(R.layout.fragment_terms_list), TermsListAdapter.OnItemClickListener {

    private var _binding: FragmentTermsListBinding? = null
    private val binding get() = _binding!!

    private val terms = listOf(
        TermUI("Абсцисса"),
        TermUI("Аксиома"),
        TermUI("Аксонометрия"),
        TermUI("Алгебра"),
        TermUI("Алгоритм"),
        TermUI("Апофема"),
        TermUI("Аппликата")
    )

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTermsListBinding.bind(view)

        // FIXME() toggle button disappears after closing filter dialog
        setHasOptionsMenu(true)
        initNavigationDrawer(binding)

        binding.termsListRecyclerView.adapter = TermsListAdapter(terms, this)
    }

    private fun initNavigationDrawer(binding: FragmentTermsListBinding) {
        toggle = ActionBarDrawerToggle(activity, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true) // for converting hamburger to back arrow

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.chosen_terms -> {
                    val action = TermsListFragmentDirections.actionTermsListFragmentToChosenTermsFragment()
                    findNavController().navigate(action)
                }
                R.id.test -> {
                    val action = TermsListFragmentDirections.actionTermsListFragmentToTestFragment()
                    findNavController().navigate(action)
                }
                R.id.add_term -> {
                    val action = TermsListFragmentDirections.actionTermsListFragmentToAddTermFragment()
                    findNavController().navigate(action)
                }
                R.id.extract_term -> Toast.makeText(requireContext(), R.string.extract_terms, Toast.LENGTH_SHORT).show()
                R.id.settings -> Toast.makeText(requireContext(), R.string.settings, Toast.LENGTH_SHORT).show()
                R.id.import_terms -> Toast.makeText(requireContext(), R.string.import_terms, Toast.LENGTH_SHORT).show()
                R.id.contact -> Toast.makeText(requireContext(), R.string.contact, Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_terms_list, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true
            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(requireContext(), newText, Toast.LENGTH_SHORT).show()
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) { return true }
        when(item.itemId) {
            R.id.action_filter -> {
                Toast.makeText(requireContext(), "Фильтр", Toast.LENGTH_SHORT).show()
                val action = TermsListFragmentDirections.actionTermsListFragmentToTermsListFilterFragment()
                findNavController().navigate(action)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(term: TermUI, changeChosenState: Boolean) {
        if (changeChosenState) {
            Toast.makeText(requireContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show()
        } else {
            val action = TermsListFragmentDirections.actionTermsListFragmentToTermDetailFragment(term, term.name)
            findNavController().navigate(action)
        }
    }
}