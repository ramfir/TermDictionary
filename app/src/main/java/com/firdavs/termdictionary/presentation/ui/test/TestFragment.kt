package com.firdavs.termdictionary.presentation.ui.test

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.firdavs.termdictionary.R
import com.firdavs.termdictionary.databinding.FragmentTestBinding
import com.firdavs.termdictionary.domain.QuestionService

class TestFragment: Fragment(R.layout.fragment_test) {

    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!

    private var currentQuestion = 0
    private var errors = 0
    private val questions = QuestionService.getQuestions()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTestBinding.bind(view)

        setQuestion(currentQuestion)
        binding.answerButton.setOnClickListener {
            if (binding.answerButton.text == "Ответить") {
                checkAnswer(view.findViewById(binding.options.checkedRadioButtonId))
                changeButton("Далее", R.color.white, R.color.red_500)
            } else if (binding.answerButton.text == "Далее") {
                currentQuestion++
                if (currentQuestion < questions.size) {
                    setQuestion(currentQuestion)
                    changeButton("Ответить", R.color.red_500, R.color.white)
                } else {
                    showResult()
                    Toast.makeText(requireContext(), "The End", Toast.LENGTH_SHORT).show()
                }
            } else {
                restart()
            }
        }
    }

    private fun restart() {
        currentQuestion = 0
        errors = 0
        binding.questionTextTextView.visibility = View.VISIBLE
        binding.options.visibility = View.VISIBLE
        setQuestion(currentQuestion)
        changeButton("Ответить", R.color.red_500, R.color.white)
    }

    private fun showResult() {
        with(binding) {
            currentQuestionTextView.text = "Вы ответили верно на ${questions.size-errors} из ${questions.size} вопросов"
            questionTextTextView.visibility = View.INVISIBLE
            options.visibility = View.INVISIBLE
            answerButton.text = "Заново"
        }
    }

    private fun changeButton(text: String, textColor: Int, backgroundColor: Int) {
        binding.answerButton.apply {
            this.text = text
            setTextColor(ContextCompat.getColor(requireContext(), textColor))
            backgroundTintList = AppCompatResources.getColorStateList(requireContext(), backgroundColor)
        }
    }

    private fun checkAnswer(radioButton: RadioButton) {
        val answer = questions[currentQuestion].answer
        val userAnswer = radioButton.text
        if (answer == userAnswer) {
            Toast.makeText(requireContext(), "Верно", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Неверно\nПравильный ответ: $answer", Toast.LENGTH_LONG).show()
            errors++
        }
    }

    private fun setQuestion(currentQuestion: Int) {
        with(binding) {
            currentQuestionTextView.text = "Вопрос ${this@TestFragment.currentQuestion +1}"
            questionTextTextView.text = questions[currentQuestion].text
            option1.text = questions[currentQuestion].option1
            option2.text = questions[currentQuestion].option2
            option3.text = questions[currentQuestion].option3
            option4.text = questions[currentQuestion].option4
        }
    }
}