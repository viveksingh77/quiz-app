package com.vivekishere.arsimple.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.vivekishere.arsimple.R
import com.vivekishere.arsimple.databinding.FragmentQuizBinding
import com.vivekishere.arsimple.pojo.questions
import com.vivekishere.arsimple.utils

class QuizFragment : Fragment() {
    private lateinit var binding: FragmentQuizBinding
    private var currentQuiz = 0
    private lateinit var CategoryId: String
    private var Categorycoins: Long = 0L
    private lateinit var quizList: ArrayList<questions>
    private var correctanswer = 0
    private var resultData:String =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(layoutInflater)
        CategoryId = arguments?.getString("cid")!!
        Categorycoins = arguments?.getLong("cc")!!
        fetchQuiz()
        onNextBtnClick()
        onBackClick()
        return binding.root
    }

    private fun onBackClick() {
        binding.close.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onNextBtnClick() {
        binding.nextBtn.setOnClickListener {
            disableBackground()
            currentQuiz++
            if (currentQuiz < quizList.size) {
                // If there are more quizzes display the next quiz
                showQuiz(quizList[currentQuiz])
            }
        }
    }

    private fun navigateScore() {
        findNavController().navigate(
            R.id.action_quizFragment_to_resultFragment , bundleOf(
            "correct" to correctanswer,
            "total" to quizList.size ,
            "score" to resultData
        ))
    }

    private fun checkQuizInFirestore() {
        if (quizList.isEmpty()) {
            binding.error.visibility = View.VISIBLE
            binding.questionview.visibility = View.GONE
        } else {
            binding.error.visibility = View.GONE
            binding.questionview.visibility = View.VISIBLE
            showQuiz(quizList[currentQuiz])
        }
    }

    private fun showQuiz(quiz: questions) {
        binding.apply {
            questiontext.text = quiz.question
            option1text.text = quiz.options?.get(0)
            option2text.text = quiz.options?.get(1)
            option3text.text = quiz.options?.get(2)
            option4text.text = quiz.options?.get(3)
            qcounter.text = getFormattedQuestionCount(currentQuiz, quizList.size)
            option1text.setOnClickListener { onOptionClick(binding.option1text, quizList[currentQuiz].options?.get(0)) }
            option2text.setOnClickListener { onOptionClick(binding.option2text, quizList[currentQuiz].options?.get(1)) }
            option3text.setOnClickListener { onOptionClick(binding.option3text, quizList[currentQuiz].options?.get(2)) }
            option4text.setOnClickListener { onOptionClick(binding.option4text, quizList[currentQuiz].options?.get(3)) }
        }
    }
    private fun getFormattedQuestionCount(currentQuiz: Int, totalQuestions: Int): String {
        val formattedCurrentQuiz = (currentQuiz + 1).toString().padStart(2, '0')
        val formattedTotalQuestions = totalQuestions.toString().padStart(2, '0')
        return "Question $formattedCurrentQuiz/$formattedTotalQuestions"
    }

    private fun onOptionClick(optionView: TextView, selectedOption: String?) {
        // Check if the selected option is correct
        if (selectedOption == quizList[currentQuiz].correctoption) {
            // If correct, change the background to green
            optionView.setBackgroundResource(R.drawable.strokebg)
            correctanswer++
        } else {
            // If incorrect, change the background to red
            optionView.setBackgroundResource(R.drawable.redstroke)
            //vibrate
            utils.vibrateOnWrongOption(requireContext())

            // Find the correct option TextView and change its background to green
            when (quizList[currentQuiz].correctoption) {
                quizList[currentQuiz].options?.get(0) -> binding.option1text.setBackgroundResource(R.drawable.strokebg)
                quizList[currentQuiz].options?.get(1) -> binding.option2text.setBackgroundResource(R.drawable.strokebg)
                quizList[currentQuiz].options?.get(2) -> binding.option3text.setBackgroundResource(R.drawable.strokebg)
                quizList[currentQuiz].options?.get(3) -> binding.option4text.setBackgroundResource(R.drawable.strokebg)
            }
        }
        disableOptionClicks()
        if (currentQuiz == quizList.size - 1) {
            // If it is the last quiz, check the score
            storeCoins()
        }

    }

    private fun storeCoins() {
        if(isPassingScore(quizList.size , correctanswer)){
            FirebaseFirestore.getInstance().collection("users").document(utils.getAuthInstance().uid!!)
                .update("coins" , FieldValue.increment(Categorycoins))
                .addOnSuccessListener {
                    resultData = "coins added to your wallet Enjoy!!"
                    navigateScore()
                }
        } else{
            resultData="you scored less than 60% try next time!!"
            navigateScore()
        }
    }

    fun isPassingScore(totalQuestions: Int, correctAnswers: Int): Boolean {
        val percentage = (correctAnswers.toFloat() / totalQuestions) * 100
        return percentage >= 60
    }

    private fun disableBackground(){
        binding.apply {
           option1text.setBackgroundResource(R.drawable.greystroke)
           option2text.setBackgroundResource(R.drawable.greystroke)
           option3text.setBackgroundResource(R.drawable.greystroke)
           option4text.setBackgroundResource(R.drawable.greystroke)
        }
    }

    private fun disableOptionClicks() {
        binding.option1text.isClickable = false
        binding.option2text.isClickable = false
        binding.option3text.isClickable = false
        binding.option4text.isClickable = false
    }

    private fun fetchQuiz() {
        quizList = ArrayList()
        FirebaseFirestore.getInstance().collection("category").document(CategoryId)
            .collection("quizes")
            .get().addOnSuccessListener { querySnapshot ->
                for (documentquiz in querySnapshot) {
                    val item = documentquiz.toObject(questions::class.java)
                    quizList.add(item)
                }
                checkQuizInFirestore()
            }.addOnFailureListener {
                checkQuizInFirestore()
            }
    }

}