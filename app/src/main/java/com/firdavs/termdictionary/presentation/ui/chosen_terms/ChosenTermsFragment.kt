package com.firdavs.termdictionary.presentation.ui.chosen_terms

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.databinding.FragmentChosenTermsBinding
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.presentation.model.TermUI
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class ChosenTermsFragment: Fragment(R.layout.fragment_chosen_terms) {

    private var _binding: FragmentChosenTermsBinding? = null
    private val binding get() = _binding!!

    private val terms = listOf(
        Term(0, "Абсцисса", "ddd", "dddd", "ffff", false),
        Term(0, "Аксиома", "ddd", "dddd", "ffff",  false),
        Term(0, "Аксонометрия", "ddd", "dddd", "ffff", false),
        Term(0, "Алгебра", "ddd", "dddd", "ffff",  false),
        Term(0, "Алгоритм", "ddd", "dddd", "ffff", false),
        Term(0, "Апофема", "ddd", "dddd", "ffff", false),
        Term(0, "Аппликата", "ddd", "dddd", "ffff", false)
    )
    private val chosenTermsAdapter by lazy {
        AsyncListDifferDelegationAdapter(getChosenTermsDiffCallback(), getChosenTermsAdapterDelegate {
            println("mmm ${it.name} ${it.definition.substring(0, 5)} ${it.translation}")
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChosenTermsBinding.bind(view)

        chosenTermsAdapter.items = terms
        binding.chosenTermsRecyclerView.adapter = chosenTermsAdapter
    }
}