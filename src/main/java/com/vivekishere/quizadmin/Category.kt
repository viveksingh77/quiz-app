package com.vivekishere.quizadmin


import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vivekishere.quizadmin.adapters.categoryAdapter
import com.vivekishere.quizadmin.databinding.CategorydialogBinding
import com.vivekishere.quizadmin.databinding.FragmentCategoryBinding
import com.vivekishere.quizadmin.model.category

class Category : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private var selectedImageUri: Uri? = null
    private val catogries = mutableListOf<category>()
    private lateinit var adapter: categoryAdapter
    private val dialogBinding by lazy {
        CategorydialogBinding.inflate(LayoutInflater.from(requireContext()))
    }
    private val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        selectedImageUri = it
        dialogBinding.image.setImageURI(it)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        onCreateCategory()
        showCategory()
        onPaymentClick()
        return binding.root
    }

    private fun onPaymentClick() {
        binding.payment.setOnClickListener {
            findNavController().navigate(R.id.action_category_to_paymentFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = categoryAdapter(
            list = catogries,
            onCategoryClicked = { catId , catname ->
                                onCategoryClicked(categoryId = catId, catName = catname)
            },
            onDeleteCategory = { catId ->
                onDeleteCategory(catId)
            })
        binding.recyclerview.adapter = adapter
    }

    private fun showCategory() {
        FirebaseFirestore.getInstance().collection("category").get()
            .addOnSuccessListener { documents ->
                catogries.clear()
                for (snapshot in documents) {
                    val category = snapshot.toObject(category::class.java)
                    catogries.add(category)
                }
                setupRecyclerView()
            }
    }

    private fun onCreateCategory() {
        binding.createCategory.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val customDialog = Dialog(requireContext())
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCancelable(true)
        customDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        customDialog.window?.setBackgroundDrawable(context?.getDrawable(R.drawable.roundbg))
        dialogBinding.chooseImg.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        dialogBinding.Addcategory.setOnClickListener {
            val category = category(
                categoryname = dialogBinding.categoryname.text.toString(),
                coins = dialogBinding.coins.text.toString().toLong(),
                Image = selectedImageUri?.toString() ?: "",
                description = dialogBinding.shortdescription.text.toString()
            )
            addToFireStore(category)
            customDialog.dismiss()
        }
        customDialog.setOnDismissListener {
            val parentViewGroup = dialogBinding.root.parent as? ViewGroup
            parentViewGroup?.removeAllViews()
        }
        customDialog.show()

    }

    private fun addToFireStore(category: category) {
        Utils.showDialog(requireContext(), "Uploading...")
        val reference = FirebaseStorage.getInstance().reference.child("categoryImage")
            .child(category.categoryname!!)
        reference.putFile(category.Image!!.toUri()).addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener {
                category.Image = it.toString()
                val path = FirebaseFirestore.getInstance().collection("category").document()
                category.id = path.id
                path.set(category)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showCategory()
                            Utils.showCustomToast(requireContext(), "category uploaded")
                            Utils.hideDialog()
                        } else {
                            Utils.showCustomToast(requireContext(), "failed to upload")
                            Utils.hideDialog()
                        }
                    }

            }
        }
    }

    private fun onDeleteCategory(categoryId: String) {
        FirebaseFirestore.getInstance().collection("category").document(categoryId).delete()
            .addOnSuccessListener {
                Utils.showCustomToast(requireContext(), "Category deleted")
                showCategory()
            }
    }

    private fun onCategoryClicked(categoryId: String ,catName:String) {
        val bundle = bundleOf(
            "catId" to categoryId ,
            "catname" to catName)
        findNavController().navigate(R.id.action_category_to_questionsFragment, bundle)
    }

}