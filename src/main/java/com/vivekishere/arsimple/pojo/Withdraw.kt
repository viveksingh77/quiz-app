package com.vivekishere.arsimple.pojo

data class Withdraw(
    val withdrawid:String?=null,
    val authid:String,
    val name:String,
    val coins:Long,
    val timestamp:String,
    val number:String,
    val status:String
)
