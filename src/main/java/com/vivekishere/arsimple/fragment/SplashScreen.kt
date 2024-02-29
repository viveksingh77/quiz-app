package com.vivekishere.arsimple.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vivekishere.arsimple.AuthViewmodel
import com.vivekishere.arsimple.R
import com.vivekishere.arsimple.activity.quizActivity
import com.vivekishere.arsimple.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.launch

class SplashScreen : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    private val viewmodel:AuthViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashScreenBinding.inflate(layoutInflater)

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                viewmodel.isCurrentUser.collect{
                    if (it){
                        startActivity(Intent(requireContext(),quizActivity::class.java))
                        requireActivity().finish()
                    } else{
                        findNavController().navigate(R.id.action_splashScreen_to_signinScreen)
                    }
                }
            }

        } , 1200)
        return binding.root
    }

}