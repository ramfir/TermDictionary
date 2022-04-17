package com.firdavs.termdictionary.presentation.ui.terms_list

import androidx.recyclerview.widget.DiffUtil
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.databinding.ItemTermBinding
import com.firdavs.termdictionary.presentation.model.TermUI
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun getTermsAdapterDelegate(itemClickListener: (TermUI, Boolean) -> Unit) = adapterDelegateViewBinding<TermUI, TermUI, ItemTermBinding>(
        { layoutInflater, root -> ItemTermBinding.inflate(layoutInflater, root, false) }
) {

    binding.root.setOnClickListener { itemClickListener.invoke(item, false) }
    binding.chosenTermImageView.setOnClickListener { itemClickListener.invoke(item, true) }

    bind {
        binding.termNameTextView.text = item.name
        if (item.isChosen) {
            binding.chosenTermImageView.setImageResource(R.drawable.img_star_checked)
        } else {
            binding.chosenTermImageView.setImageResource(R.drawable.img_star)
        }
    }
}

fun getTermsDiffCallback() = object : DiffUtil.ItemCallback<TermUI>() {
    override fun areItemsTheSame(oldItem: TermUI, newItem: TermUI) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: TermUI, newItem: TermUI) = oldItem == newItem
}