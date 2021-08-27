package com.vnsoft.asm_crawler.model.response

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message") val message: String
)