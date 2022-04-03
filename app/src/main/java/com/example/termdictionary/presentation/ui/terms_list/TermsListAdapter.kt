package com.example.termdictionary.presentation.ui.terms_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.termdictionary.databinding.ItemTermBinding
import com.example.termdictionary.domain.model.Term

class TermsListAdapter(private val terms: List<Term>): RecyclerView.Adapter<TermsListAdapter.TermViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        val binding = ItemTermBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TermViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TermViewHolder, position: Int) = holder.bind(terms[position])
    override fun getItemCount() = terms.size

    class TermViewHolder(private val binding: ItemTermBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(term: Term) {
            binding.termNameTextView.text = term.name
        }
    }
}