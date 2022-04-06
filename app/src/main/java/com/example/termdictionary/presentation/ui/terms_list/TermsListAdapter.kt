package com.example.termdictionary.presentation.ui.terms_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.termdictionary.databinding.ItemTermBinding
import com.example.termdictionary.domain.model.Term
import com.example.termdictionary.presentation.model.TermUI

class TermsListAdapter(
    private val terms: List<TermUI>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TermsListAdapter.TermViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        val binding = ItemTermBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TermViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TermViewHolder, position: Int) =
        holder.bind(terms[position])

    override fun getItemCount() = terms.size

    inner class TermViewHolder(private val binding: ItemTermBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val term = terms[position]
                    listener.onItemClick(term)
                }
            }
        }

        fun bind(term: TermUI) {
            binding.termNameTextView.text = term.name
        }
    }

    interface OnItemClickListener {
        fun onItemClick(term: TermUI)
    }
}