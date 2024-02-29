package com.vivekishere.arsimple.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.vivekishere.arsimple.databinding.ActivityMainBinding
import com.vivekishere.arsimple.utils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        utils.setStatusBarColor(this ,Color.parseColor("#232526"))
    }
}