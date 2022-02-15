package com.example.android.simplebankingapp.pojo



data class TransfersProcessData(
    var sender:String,
    var receiver: String,
    var receiverEmail: String,
    var amount: Int,
    var status:Int
)