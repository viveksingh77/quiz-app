package com.vivekishere.quizadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vivekishere.quizadmin.databinding.CategoryitemBinding
import com.vivekishere.quizadmin.model.category

class categoryAdapter(
    var list: List<category>,
    val onCategoryClicked: (String,String) -> Unit,
   val  onDeleteCategory:(String)->Unit
): RecyclerView.Adapter<categoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder( val binding:CategoryitemBinding):ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoryitemBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentlistitem = list[position]
        holder.binding.categoryname.text = currentlistitem.categoryname
        holder.binding.categoryname.setOnClickListener {
            onCategoryClicked.invoke(currentlistitem.id!! , currentlistitem.categoryname!!)
        }
        holder.binding.delete.setOnClickListener {
            onDeleteCategory(currentlistitem.id!!)
        }
    }
}