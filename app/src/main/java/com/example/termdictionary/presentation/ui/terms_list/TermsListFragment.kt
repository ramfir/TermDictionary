package com.example.termdictionary.presentation.ui.terms_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.termdictionary.R
import com.example.termdictionary.databinding.FragmentTermsListBinding
import com.example.termdictionary.domain.model.Term

class TermsListFragment : Fragment(R.layout.fragment_terms_list) {

    private val terms = listOf(
        Term("Абсцисса"),
        Term("Аксиома"),
        Term("Аксонометрия"),
        Term("Алгебра"),
        Term("Алгоритм"),
        Term("Апофема"),
        Term("Аппликата")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTermsListBinding.bind(view)
        binding.termsListRecyclerView.adapter = TermsListAdapter(terms)
    }
}