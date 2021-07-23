package com.vnsoft.exam.view_model

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vnsoft.exam.R
import com.vnsoft.exam.injection.DaggerViewModelInjector
import com.vnsoft.exam.injection.NetworkModule
import com.vnsoft.exam.injection.ViewModelInjector
import com.vnsoft.exam.model.response.Message
import com.vnsoft.exam.ui.login.LoginViewModel
import com.vnsoft.exam.ui.main.MainViewModel
import com.vnsoft.exam.utils.GsonUtils
import com.vnsoft.exam.utils.myapp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.net.ssl.HttpsURLConnection

abstract class BaseViewModel : ViewModel() {

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val responseMessage: MutableLiveData<String?> = MutableLiveData()

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .applicationComponent(myapp!!)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is LoginViewModel -> injector.inject(this)
            is MainViewModel -> injector.inject(this)
        }
    }

    protected fun onRetrievePostListStart() {
        isLoading.value = true
        errorMessage.value = null
    }

    protected fun onRetrievePostListFinish() {
        Handler(Looper.getMainLooper()).postDelayed({
            isLoading.value = false
        }, 1000)
    }

    protected fun handleApiError(error: Throwable?) {
        if (error == null) {
            errorMessage.value = R.string.api_default_error
            return
        }

        if (error is HttpException) {
            Log.w("ERROR", error.message() + error.code())
            when (error.code()) {
                HttpURLConnection.HTTP_BAD_REQUEST -> try {
                    val message: Message = GsonUtils.deserialize(
                        error.response()?.errorBody()?.string(),
                        Message::class.java
                    )
                    responseMessage.value = message.message
                } catch (e: IOException) {
                    e.printStackTrace()
                    responseMessage.value = error.message
                }
                HttpsURLConnection.HTTP_UNAUTHORIZED -> errorMessage.value = R.string.str_authe
                HttpsURLConnection.HTTP_FORBIDDEN, HttpsURLConnection.HTTP_INTERNAL_ERROR, HttpsURLConnection.HTTP_NOT_FOUND -> responseMessage.value =
                    error.message
                else -> responseMessage.value = error.message
            }
        } else if (error is SocketTimeoutException) {
            errorMessage.value = R.string.text_all_has_error_timeout
        } else if (error is IOException) {
            errorMessage.value = R.string.text_all_has_error_network
        } else {
            if (!TextUtils.isEmpty(error.message)) {
                responseMessage.value = error.message
            } else {
                errorMessage.value = R.string.text_all_has_error_please_try
            }
        }
    }

    fun toMultipartBody(name: String, file: File): MultipartBody.Part {
        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, reqFile)
    }

    fun toMultipartBody1(name: String, file: File): MultipartBody.Part {
        val reqFile = file.asRequestBody("video/*, image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, reqFile)
    }

}