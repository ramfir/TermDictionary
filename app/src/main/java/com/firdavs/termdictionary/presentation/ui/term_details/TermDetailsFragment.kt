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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)

        val term = arguments?.get("term") ?: TermUI("nothing", "ddd", "dddd", "ffff", "fdfd", false)

        val binding = FragmentTermDetailsBinding.bind(view)

        //setupViews(binding)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_term_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
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

    /*private fun setupViews(binding: FragmentTermDetailsBinding) {
        with(binding) {
            subject.title.text = "Раздел"
            subject.data.text = "(Математика)"
            definition.title.text = "Определение"
            definition.data.text = "(Определение)"
            translation.title.text = "Перевод на английский"
            translation.data.text = "(Перевод)"

            imageViewPen.setOnClickListener {
                materialCardView.strokeColor =
                    ContextCompat.getColor(requireContext(), R.color.grey)
                editTextNotes.isFocusableInTouchMode = true
            }

        }
    }*/
}