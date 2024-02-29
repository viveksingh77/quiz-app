package com.vivekishere.arsimple.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vivekishere.arsimple.databinding.FragmentResultBinding


class ResultFragment : Fragment() {
    private lateinit var binding:FragmentResultBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(layoutInflater)
       val correct = arguments?.getInt("correct")!!
       val total = arguments?.getInt("total")!!
        val score = arguments?.getString("score")!!
        binding.result.text = getFormattedResult(correct , total)
        binding.score.text = score
        return binding.root
    }

    private fun getFormattedResult(correctQuiz: Int, totalQuestions: Int): String {
        val formattedCorrectQuiz = correctQuiz.toString().padStart(2, '0')
        val formattedTotalQuestions = totalQuestions.toString().padStart(2, '0')
        return "$formattedCorrectQuiz/$formattedTotalQuestions"
    }

}