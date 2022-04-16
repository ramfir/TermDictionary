package com.firdavs.termdictionary.presentation.ui.terms_list

import androidx.recyclerview.widget.DiffUtil
import com.firdavs.termdictionary.databinding.ItemTermBinding
import com.firdavs.termdictionary.domain.model.Term
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun getTermsAdapterDelegate(itemClickListener: (Term) -> Unit) = adapterDelegateViewBinding<Term, Term, ItemTermBinding>(
        { layoutInflater, root -> ItemTermBinding.inflate(layoutInflater, root, false) }
) {

    binding.root.setOnClickListener { itemClickListener.invoke(item) }

    bind {
        binding.termNameTextView.text = item.name
    }
}

fun getTermsDiffCallback() = object : DiffUtil.ItemCallback<Term>() {
    override fun areItemsTheSame(oldItem: Term, newItem: Term) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Term, newItem: Term) = oldItem == newItem
}