package com.vivekishere.arsimple

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.vivekishere.arsimple.databinding.CustomToastBinding
import com.vivekishere.arsimple.databinding.ProgressDialogBinding


object utils {
    private var dialog: AlertDialog? = null

    fun setStatusBarColor(activity: Activity, color: Int) {
        activity.window?.apply {
            statusBarColor = color
        }
    }

    fun showDialog(context: Context, message: String) {
        val progress = ProgressDialogBinding.inflate(LayoutInflater.from(context))
        dialog = AlertDialog.Builder(context).setView(progress.root).setCancelable(false).create()
        progress.message.text = message
        dialog!!.show()
    }

    fun hideDialog() {
        dialog?.dismiss()
    }

    fun showCustomToast(context: Context, message: String) {
        val binding: CustomToastBinding =
            CustomToastBinding.inflate(LayoutInflater.from(context))
        binding.messge.text = message
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = binding.root
        toast.show()
    }
    private var firebaseAuthInstance: FirebaseAuth? = null
    fun getAuthInstance(): FirebaseAuth {
        if (firebaseAuthInstance == null) {
            firebaseAuthInstance = FirebaseAuth.getInstance()
        }
        return firebaseAuthInstance!!
    }
    fun vibrateOnWrongOption(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        vibrator?.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    }

}