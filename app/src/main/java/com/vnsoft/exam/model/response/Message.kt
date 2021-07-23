package com.vnsoft.exam.model.response

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message") val message: String
)