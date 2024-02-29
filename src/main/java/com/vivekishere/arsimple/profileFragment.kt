package com.vivekishere.arsimple

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vivekishere.arsimple.databinding.FragmentProfileBinding
import com.vivekishere.arsimple.databinding.InfodialogBinding
import com.vivekishere.arsimple.pojo.User
import com.vivekishere.arsimple.pojo.Withdraw
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class profileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        utils.showDialog(requireContext(), "uploading...")
        val imagepath = FirebaseStorage.getInstance().reference.child("userimage")
            .child(utils.getAuthInstance().uid!!)
        imagepath.putFile(it!!).addOnSuccessListener {
            imagepath.downloadUrl.addOnSuccessListener { uri ->
                FirebaseFirestore.getInstance().collection("users")
                    .document(utils.getAuthInstance().uid!!).update("profileimage", uri.toString())
                    .addOnSuccessListener {
                        utils.showCustomToast(requireContext(), " profile uploaded")
                        utils.hideDialog()
                        fetchdetails()
                    }
            }
        }
    }
    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        setupProfile()
        fetchdetails()
        withdrawAmount()
        showinfo()
        return binding.root
    }

    private fun showinfo() {
        binding.info.setOnClickListener {
            val dialog = Dialog(requireContext())
            val dialogBinding = InfodialogBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.setCancelable(true)
            dialog.show()
        }
    }

    private fun withdrawAmount() {
        binding.withdraw.setOnClickListener {
            val totalcoins = binding.usercoin.text.toString().toLong()
            val withdrawamount = binding.amount.text.toString().toLong()
            if (totalcoins >= 5000L && withdrawamount >= 5000 && withdrawamount <= totalcoins) {
                proceedPayment(totalcoins, withdrawamount)
            } else {
                binding.amount.error = "Insufficient coins"
            }
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
            val date = Date(timestamp)
            return sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


    private fun proceedPayment(totalcoins: Long, amount: Long) {
        val updatedamount = totalcoins - amount

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(utils.getAuthInstance().uid!!)
            .update("coins", updatedamount)
            .addOnSuccessListener {

                val path = FirebaseFirestore.getInstance().collection("withdraw")
                    .document()
                val withdraw = Withdraw(
                    withdrawid = path.id,
                    authid = utils.getAuthInstance().uid!!,
                    name = username,
                    coins = amount,
                    status = "pending",
                    number = binding.number.text.toString(),
                    timestamp = formatTimestamp(System.currentTimeMillis())
                )
                path.set(withdraw)
                    .addOnSuccessListener {
                        utils.showCustomToast(requireContext(), "withdraw successful")
                        binding.amount.text.clear()
                        binding.number.text.clear()
                        fetchdetails()
                    }
            }
            .addOnFailureListener {
                utils.showCustomToast(requireContext(), "server problem try later")
            }
    }

    private fun fetchdetails() {
        FirebaseFirestore.getInstance().collection("users")
            .document(utils.getAuthInstance().uid!!).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                binding.apply {
                    usercoin.text = user?.coins.toString()
                    username = user?.username!!
                    Glide.with(requireContext()).load(user.profileimage)
                        .placeholder(R.drawable.avatar2).into(profileImage)
                }
            }
    }

    private fun setupProfile() {
        binding.profileImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

}