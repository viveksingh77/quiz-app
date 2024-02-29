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
import com.vivekishere.arsimple.databinding.FragmentSignUpScreenBinding
import com.vivekishere.arsimple.utils
import kotlinx.coroutines.launch


class SignUpScreen : Fragment() {
    private lateinit var binding: FragmentSignUpScreenBinding
    private val viewmodel:AuthViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpScreenBinding.inflate(layoutInflater)


        signinClicked()
        onRegisterClicked()
        return binding.root
    }

    private fun onRegisterClicked() {
        binding.signupBtn.setOnClickListener {
            utils.showDialog(requireContext() , "registering...")
            viewmodel.SignUpWithEmailPassword(name = binding.editTextname.text.toString() , email = binding.editTextEmail.text.toString() , password =  binding.editTextpassword.text.toString() , coins = 0L)
            lifecycleScope.launch {
                viewmodel.isSignedUp.collect{
                    if (it){
                        utils.hideDialog()
                        startActivity(Intent(requireContext() , quizActivity::class.java))
                    }else{
                        utils.hideDialog()
                        utils.showCustomToast(requireContext() , "register failed")
                    }
                }
            }
        }
    }

    private fun signinClicked() {
        binding.signinBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpScreen_to_signinScreen)
        }
    }

}