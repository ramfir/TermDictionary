package com.example.termdictionary.presentation.ui.terms_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.termdictionary.R
import com.example.termdictionary.databinding.FragmentTermsListBinding
import com.example.termdictionary.domain.model.Term
import com.example.termdictionary.presentation.model.TermUI

class TermsListFragment : Fragment(R.layout.fragment_terms_list), TermsListAdapter.OnItemClickListener {

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

        val binding = FragmentTermsListBinding.bind(view)
        binding.termsListRecyclerView.adapter = TermsListAdapter(terms, this)
    }

    override fun onItemClick(term: TermUI) {
        val action = TermsListFragmentDirections.actionTermsListFragmentToTermDetailFragment(term, term.name)
        findNavController().navigate(action)
    }
}