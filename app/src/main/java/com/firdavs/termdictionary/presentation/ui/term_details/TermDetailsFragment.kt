package com.firdavs.termdictionary.presentation.ui.term_details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.databinding.FragmentTermDetailsBinding
import com.firdavs.termdictionary.presentation.model.TermUI

class TermDetailsFragment : Fragment(R.layout.fragment_term_details) {

    private var _binding: FragmentTermDetailsBinding? = null
    private val binding get() = _binding!!

    private var term: TermUI? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)

        term = arguments?.get("term") as TermUI

        _binding = FragmentTermDetailsBinding.bind(view)

        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_term_details, menu)
        setIcon(menu.findItem(R.id.star))

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setIcon(item: MenuItem) {
        if (term?.isChosen == true) {
            item.setIcon(R.drawable.ic_star_checked)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.star -> Toast.makeText(
                requireContext(),
                "Добавлено в избранное",
                Toast.LENGTH_SHORT
            ).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        with(binding) {
            subject.setText("Введение в специальность")
            definition.setText(term?.definition)
            translation.setText(term?.translation)
            notes.setText(term?.notes)
        }
    }
}