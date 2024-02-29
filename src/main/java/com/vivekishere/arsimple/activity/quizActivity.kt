package com.vivekishere.arsimple.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vivekishere.arsimple.databinding.ActivityQuizBinding
import com.vivekishere.arsimple.utils

class quizActivity : AppCompatActivity() {
    private lateinit var binding:ActivityQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        utils.setStatusBarColor(this , Color.parseColor("#232526"))
    }
}