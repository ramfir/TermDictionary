package com.firdavs.termdictionary.presentation.ui.chosen_terms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firdavs.termdictionary.databinding.ItermChosenTermBinding
import com.firdavs.termdictionary.presentation.model.TermUI

class ChosenTermsAdapter(
    private val terms: List<TermUI>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ChosenTermsAdapter.ChosenTermViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChosenTermViewHolder {
        val binding = ItermChosenTermBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChosenTermViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChosenTermViewHolder, position: Int) =
        holder.bind(terms[position])

    override fun getItemCount() = terms.size

    inner class ChosenTermViewHolder(private val binding: ItermChosenTermBinding) :
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
        fun onItemClick(term: TermUI, changeChosenState: Boolean = false)
    }
}