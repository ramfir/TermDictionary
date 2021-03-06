package com.firdavs.termdictionary.presentation.ui.term_details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.databinding.FragmentTermDetailsBinding
import com.firdavs.termdictionary.presentation.model.TermUI
import com.firdavs.termdictionary.presentation.mvvm.term_details.TermDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TermDetailsFragment : Fragment(R.layout.fragment_term_details) {

    private var _binding: FragmentTermDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TermDetailsViewModel by viewModel()

    private var mMenu: Menu? = null

    private var term: TermUI? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)

        term = arguments?.get("term") as TermUI

        _binding = FragmentTermDetailsBinding.bind(view)

        viewModel.termId.value = term!!.id
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_term_details, menu)
        mMenu = menu
        setIcon(menu.findItem(R.id.star))

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setIcon(item: MenuItem) {
        if (term?.isChosen == true) {
            item.setIcon(R.drawable.ic_star_checked)
        } else {
            item.setIcon(R.drawable.ic_star_border)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.star -> {
                term = term!!.copy(isChosen = !term!!.isChosen)
                viewModel.updateTerm(term!!)

                mMenu?.let { setIcon(it.findItem(R.id.star)) }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        with(binding) {
            viewModel.subjectsOfTerms.observe(viewLifecycleOwner) {
                subject.text?.clear()
                it.subjects.forEach {
                    subject.append("${it.name} | ")
                }
            }
            //subject.setText("???????????????? ?? ??????????????????????????")
            definition.setText(term?.definition)
            translation.setText(term?.translation)
            notes.setText(term?.notes)
            saveNotes.setEndIconOnClickListener {
                term = term?.copy(notes = notes.text.toString())
                viewModel.updateTerm(term!!)
                Toast.makeText(requireContext(), "?????????????? ??????????????????", Toast.LENGTH_SHORT).show()
                hideKeyboard()
                notes.clearFocus()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}