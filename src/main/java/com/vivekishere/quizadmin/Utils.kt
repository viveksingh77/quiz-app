package com.vivekishere.quizadmin

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.vivekishere.quizadmin.databinding.CustomToastBinding
import com.vivekishere.quizadmin.databinding.ProgressDialogBinding

object Utils {
    private var dialog: AlertDialog? = null
    fun setStatusBarColor(activity: Activity, color: Int) {
        activity.window?.apply {
            statusBarColor = color
        }
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
    fun showDialog(context: Context, message: String) {
        val progress = ProgressDialogBinding.inflate(LayoutInflater.from(context))
        dialog = AlertDialog.Builder(context , R.style.AlertDialogTheme).setView(progress.root).setCancelable(false).create()
        progress.message.text = message
        dialog!!.show()
    }

    fun hideDialog() {
        dialog?.dismiss()
    }
}