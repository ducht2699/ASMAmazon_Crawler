package com.vnsoft.exam.network

import com.vnsoft.exam.model.response.BaseResponse
import com.vnsoft.exam.model.request.LoginRequest
import com.vnsoft.exam.model.request.SocialRequest
import com.vnsoft.exam.model.response.User
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    /**
     * Get the list of the pots from the API
     */

    @POST("v1/auth/signin")
    fun loginManual(@Body request: LoginRequest): Observable<BaseResponse<User>>

    @POST("v1/auth/login-facebook")
    fun loginFacebook(@Body request: SocialRequest): Observable<BaseResponse<User>>

    @POST("v1/auth/login-google")
    fun loginGoogle(@Body request: SocialRequest): Observable<BaseResponse<User>>
}