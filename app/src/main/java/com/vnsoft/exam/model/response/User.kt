package com.vnsoft.exam.model.response


import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class User(): RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int? = 0
    @SerializedName("createdAt")
    var createdAt: String? = null
    @SerializedName("updatedAt")
    var updatedAt: String? = null
    @SerializedName("deleted")
    var deleted: Boolean = false
    @SerializedName("phone")
    var phone: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("displayName")
    var displayName: String? = null
    @SerializedName("password")
    var password: String? = null
    @SerializedName("social_facebook_id")
    var socialFacebookId: String? = null
    @SerializedName("social_google_id")
    var socialGoogleId: String? = null
    @SerializedName("social_apple_id")
    var socialAppleId: String? = null
    @SerializedName("phoneCountryCode")
    var phoneCountryCode: String? = null
    @SerializedName("phoneCountryName")
    var phoneCountryName: String? = null
    @SerializedName("phoneCountryDialCode")
    var phoneCountryDialCode: String? = null
    @SerializedName("avatarId")
    var avatarId: String? = null
    @SerializedName("avatar")
    var avatar: String? = null
    @SerializedName("gender")
    var gender: String? = null
    @SerializedName("address")
    var address: String? = null
    @SerializedName("status")
    var status: Int? = 0
    @SerializedName("birthday")
    var birthday: String? = null
    @SerializedName("wallet")
    var wallet: Int? = 0
    @SerializedName("score")
    var score: Int? = 0
    @SerializedName("totalWallet")
    var totalWallet: Int? = 0
    @SerializedName("totalScore")
    var totalScore: Int? = 0
    @SerializedName("roleId")
    var roleId: Int? = 0
    @SerializedName("accessToken")
    var accessToken: String? = null
    var selected: Boolean? = false
}