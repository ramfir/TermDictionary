package com.example.termdictionary.presentation.ui.chosen_terms

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.termdictionary.R
import com.example.termdictionary.databinding.FragmentChosenTermsBinding
import com.example.termdictionary.presentation.model.TermUI

class ChosenTermsFragment: Fragment(R.layout.fragment_chosen_terms), ChosenTermsAdapter.OnItemClickListener {

    private var _binding: FragmentChosenTermsBinding? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChosenTermsBinding.bind(view)

        binding.chosenTermsRecyclerView.adapter = ChosenTermsAdapter(terms, this)
    }

    override fun onItemClick(term: TermUI, changeChosenState: Boolean) {
        Toast.makeText(requireContext(), term.name, Toast.LENGTH_SHORT).show()
        val action = ChosenTermsFragmentDirections.actionChosenTermsFragmentToTermDetailFragment(term, term.name)
        findNavController().navigate(action)
    }
}