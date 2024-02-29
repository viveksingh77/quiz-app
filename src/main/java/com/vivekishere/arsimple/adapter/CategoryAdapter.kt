package com.vivekishere.arsimple.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vivekishere.arsimple.databinding.CategoryitemBinding
import com.vivekishere.arsimple.pojo.category

class CategoryAdapter(
    val context: Context,
    private val categorylist: List<category>,
    val onCategoryClick:(String ,Long)->Unit
    ) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(val binding: CategoryitemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryitemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return categorylist.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentcategory = categorylist[position]
        holder.binding.apply {
            Glide.with(context).load(currentcategory.Image).into(categoryimage)
            categoryname.text = currentcategory.categoryname
            catdescription.text = currentcategory.description
            catcoin.text = currentcategory.coins.toString()
        }
        holder.itemView.setOnClickListener {
            onCategoryClick.invoke(currentcategory.id!! , currentcategory.coins!!)
        }
    }
}