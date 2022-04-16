package com.firdavs.termdictionary.presentation.ui.chosen_terms

import androidx.recyclerview.widget.DiffUtil
import com.firdavs.termdictionary.databinding.ItemChosenTermBinding
import com.firdavs.termdictionary.domain.model.Term
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun getChosenTermsAdapterDelegate(itemClickListener: (Term) -> Unit) = adapterDelegateViewBinding<Term, Term, ItemChosenTermBinding>(
        { layoutInflater, root -> ItemChosenTermBinding.inflate(layoutInflater, root, false) }
) {

    binding.root.setOnClickListener { itemClickListener.invoke(item) }

    bind {
        binding.termNameTextView.text = item.name
    }
}

fun getChosenTermsDiffCallback() = object : DiffUtil.ItemCallback<Term>() {
    override fun areItemsTheSame(oldItem: Term, newItem: Term) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Term, newItem: Term) = oldItem == newItem
}