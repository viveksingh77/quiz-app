package com.vivekishere.quizadmin.model

data class questions(
    val questionid:String?=null,
    val question: String?=null,
    val options: ArrayList<String>?=null,
    val correctoption: String?=null
)
