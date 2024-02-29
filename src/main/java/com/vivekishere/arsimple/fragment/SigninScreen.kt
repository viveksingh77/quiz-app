package com.vivekishere.arsimple.fragment

import android.content.Intent
import android.os.Bundle
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
import com.vivekishere.arsimple.databinding.FragmentSigninScreenBinding
import com.vivekishere.arsimple.utils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SigninScreen : Fragment() {
    private lateinit var binding:FragmentSigninScreenBinding
    private val viewmodel:AuthViewmodel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninScreenBinding.inflate(layoutInflater)
        signUpClicked()
        onLoginClicked()
        return binding.root
    }

    private fun onLoginClicked() {
        binding.signinBtn.setOnClickListener {
            signInWithEmailPassword()
        }
    }

    private fun signInWithEmailPassword() {
        utils.showDialog(requireContext() , "signing in")
        viewmodel.signInWithEmailPassword(binding.editTextEmail.text.toString() , binding.editTextpassword.text.toString())
        lifecycleScope.launch {
            viewmodel.isSignedIn.collect{
                if (it){
                    utils.hideDialog()
                    startActivity(Intent(requireContext() , quizActivity::class.java))
                } else{
                    utils.hideDialog()
                    utils.showCustomToast(requireContext() , "user not found")
                }
            }
        }
    }

    private fun signUpClicked() {
        binding.signupBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signinScreen_to_signUpScreen)
        }
    }

}