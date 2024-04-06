package com.vivekishere.quizadmin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vivekishere.quizadmin.databinding.PaymentItemBinding
import com.vivekishere.quizadmin.model.Withdraw

class PaymentAdapter (
    var list: List<Withdraw> ,
    val onDonePaymentClicked:(Withdraw)->Unit
    ): RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {
    class PaymentViewHolder(val binding:PaymentItemBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        return PaymentViewHolder(PaymentItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {

        val currentdata:Withdraw = list[position]
        holder.binding.apply {
            paymentdata.text ="name : ${currentdata.name} \n" +
                    "coins : ${currentdata.coins} " +
                    "\nnumber : ${currentdata.number} " +
                    "\nstatus : ${currentdata.status} \n" +
                    "time : ${currentdata.timestamp}"
            donestatus.setOnClickListener {
                onDonePaymentClicked.invoke(currentdata)
                donestatus.visibility = View.GONE
            }
            if (currentdata.status =="pending"){
                donestatus.visibility = View.VISIBLE
            } else{
                donestatus.visibility = View.GONE
            }
        }
    }


}