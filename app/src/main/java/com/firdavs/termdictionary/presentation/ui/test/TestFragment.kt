package com.firdavs.termdictionary.presentation.ui.test

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.databinding.FragmentTestBinding
import com.firdavs.termdictionary.domain.model.Term
import com.firdavs.termdictionary.presentation.mvvm.test.TestFragmentViewModel
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestFragment : Fragment(R.layout.fragment_test) {

    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TestFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTestBinding.bind(view)

        viewModel.terms.observe(viewLifecycleOwner) { showTerms(it) }

        viewModel.answer.observe(viewLifecycleOwner) { showQuestion(it) }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.testEvent.collect { event ->
                when (event) {
                    is TestFragmentViewModel.TestEvent.ShowRightAnswerToast -> {
                        Toast.makeText(requireContext(), "Верно", Toast.LENGTH_SHORT).show()
                    }
                    is TestFragmentViewModel.TestEvent.ShowWrongAnswerToast -> {
                        Toast.makeText(requireContext(),
                                      "Неверно\nПравильный ответ: ${event.answer}",
                                      Toast.LENGTH_LONG)
                            .show()
                    }
                    is TestFragmentViewModel.TestEvent.ChangeButton -> changeButton(event.text,
                                                                                    event.textColor,
                                                                                    event.buttonColor)
                    is TestFragmentViewModel.TestEvent.ShowResult -> binding.currentQuestionTextView.text =
                        "Вы ответили верно на ${5 - event.errors} из ${5} вопросов"
                    is TestFragmentViewModel.TestEvent.HideElements -> hideElements()
                    is TestFragmentViewModel.TestEvent.ShowElements -> showElements()
                }
            }
        }

        binding.answerButton.setOnClickListener {
            viewModel.onButtonClicked(binding.answerButton.text.toString(),
                                      (view.findViewById(binding.options.checkedRadioButtonId) as RadioButton).text.toString())
        }
    }

    private fun showQuestion(term: Term?) =
        term?.let { binding.questionTextTextView.text = it.definition }

    private fun showTerms(terms: List<Term>?) {
        terms?.let {
            with(binding) {
                currentQuestionTextView.text = "Вопрос ${viewModel.currentQuestion}"
                option1.text = terms[0].name
                option2.text = terms[1].name
                option3.text = terms[2].name
                option4.text = terms[3].name
            }
        }
    }

    private fun changeButton(text: String, textColor: Int, backgroundColor: Int) {
        binding.answerButton.apply {
            this.text = text
            setTextColor(ContextCompat.getColor(requireContext(), textColor))
            backgroundTintList =
                AppCompatResources.getColorStateList(requireContext(), backgroundColor)
        }
    }

    private fun hideElements() {
        with(binding) {
            questionTextTextView.visibility = View.INVISIBLE
            options.visibility = View.INVISIBLE
            answerButton.text = "Заново"
        }
    }

    private fun showElements() {
        binding.questionTextTextView.visibility = View.VISIBLE
        binding.options.visibility = View.VISIBLE
        changeButton("Ответить", R.color.red_500, R.color.white)
    }
}