package com.vivekishere.arsimple

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.vivekishere.arsimple.pojo.User
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewmodel : ViewModel() {
    private val TAG = "AuthViewmodel"

    //sing up
    private val _isSignedup = MutableStateFlow(false)
    val isSignedUp = _isSignedup

    //sign in
    private val _isSignedIn = MutableStateFlow(false)
    val isSignedIn = _isSignedIn

    //checking the current user
    private val _isCurrentUser = MutableStateFlow(false)
    val isCurrentUser = _isCurrentUser

    init {
        utils.getAuthInstance().currentUser?.let {
            _isCurrentUser.value = true
        }
    }


    fun SignUpWithEmailPassword(email: String, password: String, name: String , coins:Long) {
        utils.getAuthInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = utils.getAuthInstance().currentUser
                    val userdetails = User(username = name , email = email , password = password , coins = coins , authid = user!!.uid , profileimage = null)
//                    val userMap = hashMapOf(
//                        "username" to name,
//                        "email" to email,
//                        "password" to password,
//                        "coins" to coins,
//                        "authid" to user!!.uid
//                    )
                    FirebaseFirestore.getInstance().collection("users").document(user.uid)
                        .set(userdetails)
                        .addOnSuccessListener {
                            _isSignedup.value = true
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "onFailed: ${exception.cause.toString()}")
                        }
                }
    }
}

fun signInWithEmailPassword(email: String, password: String) {
    utils.getAuthInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            _isSignedIn.value = it.isSuccessful
        }
}
}