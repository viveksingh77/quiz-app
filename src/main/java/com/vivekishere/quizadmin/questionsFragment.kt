package com.vivekishere.quizadmin

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.vivekishere.quizadmin.adapters.questionAdapter
import com.vivekishere.quizadmin.databinding.FragmentQuestionsBinding
import com.vivekishere.quizadmin.databinding.QuestionsDialogBinding
import com.vivekishere.quizadmin.model.questions

class questionsFragment : Fragment() {
    private lateinit var binding: FragmentQuestionsBinding
   private lateinit var  catID: String
   private lateinit var  catName: String
   private lateinit var questionadapter:questionAdapter
    val quizQuestions = mutableListOf<questions>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionsBinding.inflate(layoutInflater)
         catID = arguments?.getString("catId")!!
         catName = arguments?.getString("catname")!!
        // Inflate the layout for this fragment
        onFabClick()
        fetchQuestions()
        setcategoryname()
        return binding.root
    }

    private fun setUpRecyclerView() {
        questionadapter = questionAdapter(list =quizQuestions){ QID->
            onDeleteQuestion(QID)
        }
        binding.recyclerview.adapter = questionadapter
    }

    private fun onDeleteQuestion(questionID: String) {
       FirebaseFirestore.getInstance().collection("category").document(catID)
           .collection("quizes").document(questionID).delete().addOnSuccessListener {
               Utils.showCustomToast(requireContext() , "question deleted successfully")
           }
    }

    private fun setcategoryname() {
        binding.categoryname.text = catName
    }
    private fun fetchQuestions(){
        FirebaseFirestore.getInstance().collection("category").document(catID)
            .collection("quizes").get().addOnSuccessListener { querysnapshot->
                quizQuestions.clear()
               for (document in querysnapshot){
                   val questions = document.toObject(questions::class.java)
                   quizQuestions.add(questions)
               }
                setUpRecyclerView()
            }.addOnFailureListener {
                Utils.showCustomToast(requireContext(),"Failed to load data")
            }
    }

    private fun onFabClick() {
        binding.addQuestions.setOnClickListener {
            showQuestionDialog()
        }
    }

    private fun showQuestionDialog() {
        val customDialog = Dialog(requireContext())
        val dialogBinding =  QuestionsDialogBinding.inflate(LayoutInflater.from(requireContext()))
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCancelable(true)
        customDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Set up click listener for submit button
        dialogBinding.btnSubmitQuestion.setOnClickListener {
            if (dialogBinding.editTextOption1.text.isNotEmpty() &&
                dialogBinding.editTextOption2.text.isNotEmpty() &&
                dialogBinding.editTextOption3.text.isNotEmpty() &&
                dialogBinding.editTextOption4.text.isNotEmpty() &&
                dialogBinding.editTextQuestion.text.isNotEmpty()
            ) {
                // Get the selected radio button
                val correctOptionEditText = when {
                    dialogBinding.radioButtonOption1.isChecked -> dialogBinding.editTextOption1
                    dialogBinding.radioButtonOption2.isChecked -> dialogBinding.editTextOption2
                    dialogBinding.radioButtonOption3.isChecked -> dialogBinding.editTextOption3
                    dialogBinding.radioButtonOption4.isChecked -> dialogBinding.editTextOption4
                    else -> null
                }

                // Now you can use correctOptionEditText to access the correct option's EditText
                val questionText = dialogBinding.editTextQuestion.text.toString()
                val option1Text = dialogBinding.editTextOption1.text.toString()
                val option2Text = dialogBinding.editTextOption2.text.toString()
                val option3Text = dialogBinding.editTextOption3.text.toString()
                val option4Text = dialogBinding.editTextOption4.text.toString()

                // Check if correctOptionEditText is not null and its text is not empty
                if (correctOptionEditText != null && correctOptionEditText.text.isNotEmpty()) {
                    val correctOptionText = correctOptionEditText.text.toString()

                    // Show Toast or proceed with your Firestore logic here
                  val quizpath =  FirebaseFirestore.getInstance().collection("category")
                        .document(catID).collection("quizes").document()
                    //quiz
                    val question = hashMapOf(
                        "questionid" to quizpath.id,
                        "question" to questionText,
                        "options" to arrayListOf(option1Text , option2Text , option3Text , option4Text),
                        "correctoption" to correctOptionText
                    )
                    quizpath.set(question).addOnSuccessListener {
                        Utils.showCustomToast(requireContext(),"question Added")
                        customDialog.dismiss()
                        fetchQuestions()
                    }
                } else {
                    Utils.showCustomToast(requireContext(),"Please select a correct option")
                }
            } else {
                Utils.showCustomToast(requireContext() , "Please fill in all fields")
            }
        }

        customDialog.show()
    }
}