package com.vivekishere.quizadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vivekishere.quizadmin.databinding.QuestionitemBinding
import com.vivekishere.quizadmin.model.questions

class questionAdapter(
    var list: List<questions> ,
    val onQuestionDelete:(String)->Unit
    ):RecyclerView.Adapter<questionAdapter.QuestionViewHolder>() {
    class QuestionViewHolder(var binding: QuestionitemBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(QuestionitemBinding.inflate(LayoutInflater.from(parent.context) ,parent , false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val questionlistitem = list[position]
        holder.binding.questionfield.text = questionlistitem.question
        holder.binding.answerfield.text = questionlistitem.correctoption

        holder.binding.delete.setOnClickListener {
         onQuestionDelete(questionlistitem.questionid!!)
        }
    }
}