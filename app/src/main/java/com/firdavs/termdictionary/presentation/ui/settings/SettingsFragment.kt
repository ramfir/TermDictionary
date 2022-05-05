package com.firdavs.termdictionary.presentation.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.databinding.FragmentSettingsBinding
import java.util.*

class SettingsFragment: Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        binding.applyButton.setOnClickListener {
            changeLocale((view.findViewById(binding.options.checkedRadioButtonId) as RadioButton).id)
            val action = SettingsFragmentDirections.actionSettingsFragmentToTermsListFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun changeLocale(radioButtonId: Int) {
        var languageCode = "ru"
        if (radioButtonId == R.id.english_radio_button)
            languageCode = "en"
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        context?.resources?.updateConfiguration(config, context?.resources?.displayMetrics)
    }
}