package com.firdavs.termdictionary.presentation.ui.chosen_terms

import androidx.recyclerview.widget.DiffUtil
import com.firdavs.termdictionary.databinding.ItemChosenTermBinding
import com.firdavs.termdictionary.presentation.model.TermUI
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun getChosenTermsAdapterDelegate(itemClickListener: (TermUI) -> Unit) = adapterDelegateViewBinding<TermUI, TermUI, ItemChosenTermBinding>(
        { layoutInflater, root -> ItemChosenTermBinding.inflate(layoutInflater, root, false) }
) {

    binding.root.setOnClickListener { itemClickListener.invoke(item) }

    bind {
        binding.termNameTextView.text = item.name
    }
}

fun getChosenTermsDiffCallback() = object : DiffUtil.ItemCallback<TermUI>() {
    override fun areItemsTheSame(oldItem: TermUI, newItem: TermUI) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: TermUI, newItem: TermUI) = oldItem == newItem
}