package com.vnsoft.asm_crawler.model.request


import com.google.gson.annotations.SerializedName

data class SocialRequest(
    @SerializedName("token")
    val token: String
)