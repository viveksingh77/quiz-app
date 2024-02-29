package com.vivekishere.arsimple.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.vivekishere.arsimple.R
import com.vivekishere.arsimple.adapter.CategoryAdapter
import com.vivekishere.arsimple.databinding.FragmentCategoryBinding
import com.vivekishere.arsimple.pojo.User
import com.vivekishere.arsimple.pojo.category
import com.vivekishere.arsimple.utils

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var adapter:CategoryAdapter
    private val categoryList = mutableListOf<category>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        fetchCategory()
        fetchUserDetails()
        movetoprofile()
        return binding.root
    }

    private fun movetoprofile() {
        binding.profileImage.setOnClickListener {
            findNavController().navigate(R.id.action_categoryFragment_to_profileFragment)
        }
    }

    private fun fetchUserDetails() {
        FirebaseFirestore.getInstance().collection("users")
            .document(utils.getAuthInstance().uid!!).get()
            .addOnSuccessListener { document->
                val user = document.toObject(User::class.java)
                binding.apply {
                   usercoin.text = user?.coins.toString()
                    username.text = "Hey ${user?.username}"
                    Glide.with(requireContext()).load(user?.profileimage).placeholder(R.drawable.avatar2).into(profileImage)
                }
            }
    }

    private fun fetchCategory() {
        FirebaseFirestore.getInstance().collection("category").get()
            .addOnSuccessListener {querySnapshot->
                categoryList.clear()
                for (document in querySnapshot){
                    val categoryitem = document.toObject(category::class.java)
                    categoryList.add(categoryitem)
                }
                setupRecyclerView()

            }
    }
    private fun setupRecyclerView() {
        adapter = CategoryAdapter(context = requireContext(), categorylist =categoryList){cid , cc->
            onCategoryClick(categoryId = cid , categoryCoins = cc)
        }
        binding.recyclerview.adapter = adapter
    }
    private fun onCategoryClick(categoryId: String , categoryCoins:Long) {
        findNavController().navigate(
            R.id.action_categoryFragment_to_quizFragment, bundleOf("cid" to categoryId,
            "cc" to categoryCoins))
    }


}