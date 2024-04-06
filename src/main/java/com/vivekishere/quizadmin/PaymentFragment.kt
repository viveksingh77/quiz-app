package com.vivekishere.quizadmin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.vivekishere.quizadmin.adapters.PaymentAdapter
import com.vivekishere.quizadmin.databinding.FragmentPaymentBinding
import com.vivekishere.quizadmin.model.Withdraw

class PaymentFragment : Fragment() {
    private lateinit var binding:FragmentPaymentBinding
    private lateinit var paymentadapter: PaymentAdapter
    private val paymentlist = mutableListOf<Withdraw>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPaymentBinding.inflate(layoutInflater)
        fetchWithdrawAmount()
        return binding.root
    }

    private fun fetchWithdrawAmount() {
        FirebaseFirestore.getInstance().collection("withdraw").get()
            .addOnSuccessListener {querysnapshot->
                paymentlist.clear()
                for (document in querysnapshot){
                    val doc = document.toObject(Withdraw::class.java)
                    paymentlist.add(doc)
                }
                setupRecyclerView()
            }
    }

    private fun setupRecyclerView() {
        paymentadapter = PaymentAdapter(paymentlist){withdraw ->
            onDonePaymentClicked(withdraw)

        }
        binding.recyclerview.adapter = paymentadapter
    }
    private fun onDonePaymentClicked(withdraw: Withdraw) {
        val updatestatus = hashMapOf("status" to "success")
        FirebaseFirestore.getInstance().collection("withdraw").document(withdraw.withdrawid!!)
            .update(updatestatus as Map<String, Any>)
            .addOnSuccessListener {
                Utils.showCustomToast(requireContext() , "updated")
                fetchWithdrawAmount()
            }
    }

}