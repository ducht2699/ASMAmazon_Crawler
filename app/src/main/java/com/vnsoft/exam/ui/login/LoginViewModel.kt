package com.vnsoft.exam.ui.login

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.vnsoft.exam.R
import com.vnsoft.exam.data.DataManager
import com.vnsoft.exam.injection.NetworkModule
import com.vnsoft.exam.model.response.BaseResponse
import com.vnsoft.exam.model.constants.TypeLogin
import com.vnsoft.exam.model.request.LoginRequest
import com.vnsoft.exam.model.request.SocialRequest
import com.vnsoft.exam.model.response.User
import com.vnsoft.exam.network.Api
import com.vnsoft.exam.utils.PREF_ACCOUNT
import com.vnsoft.exam.utils.PREF_LOGIN_TYPE
import com.vnsoft.exam.utils.PREF_PASS
import com.vnsoft.exam.view_model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@SuppressLint("CheckResult")
class LoginViewModel(): BaseViewModel() {
    @Inject
    lateinit var dataManager: DataManager
    @Inject
    lateinit var api: Api

    val loginResponse =  MutableLiveData<BaseResponse<User>>()

    fun login(request: LoginRequest){
        api.loginManual(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { result ->
                    dataManager.save(PREF_ACCOUNT, request.email)
                    dataManager.save(PREF_PASS, request.password)
                    dataManager.save(PREF_LOGIN_TYPE, TypeLogin.MANUAL.value)

                    dataManager.clear(User::class.java)
                    dataManager.save(result.body)

                    NetworkModule.mToken = result.body.accessToken!!
                    loginResponse.postValue(result) },
                {throwable->
                    handleApiError(throwable)
                }
            )
    }


    fun loginFacebook(request: SocialRequest){
        api.loginFacebook(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { result ->
                    dataManager.save(PREF_LOGIN_TYPE, TypeLogin.FACEBOOK.value)

                    dataManager.clear(User::class.java)
                    dataManager.save(result.body)

                    NetworkModule.mToken = result.body.accessToken!!
                    loginResponse.postValue(result) },
                {throwable->
                    handleApiError(throwable)
                }
            )
    }

    fun loginGoogle(request: SocialRequest){
        api.loginGoogle(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { result ->
                    dataManager.save(PREF_LOGIN_TYPE, TypeLogin.GOOGLE.value)

                    dataManager.clear(User::class.java)
                    dataManager.save(result.body)

                    NetworkModule.mToken = result.body.accessToken!!
                    loginResponse.postValue(result) },
                {throwable->
                    handleApiError(throwable)
                }
            )
    }

    fun autoLogin(context: Context){
        val type = dataManager.getInt(PREF_LOGIN_TYPE)
        if (type == TypeLogin.FACEBOOK.value){
            val accessToken = AccessToken.getCurrentAccessToken()
            if (accessToken != null && accessToken.token != null)
                loginFacebook(SocialRequest(accessToken.token))
        }else if (type == TypeLogin.GOOGLE.value){
            refreshIdTokenGoogle(context)
        }else{
            val email = dataManager.getString(PREF_ACCOUNT)
            val password = dataManager.getString(PREF_PASS)
            if (email == null){
                responseMessage.postValue("error")
            }else
                login(LoginRequest(email, password))
        }
    }

    private fun refreshIdTokenGoogle(context: Context) {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.server_client_id))
                .requestEmail()
                .build()
        val mGoogleApiClient = GoogleApiClient.Builder(context)
            .enableAutoManage(
                context as FragmentActivity /* FragmentActivity */
                /* OnConnectionFailedListener */
            ) { }
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
        val opr =
            Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient)
        if (opr.isDone) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            val result = opr.get()
            //                            handleSignInResult(result);  // result.getSignInAccount().getIdToken(), etc.
            val googleSignInAccount = result.signInAccount
            loginGoogle(SocialRequest(googleSignInAccount!!.idToken!!))
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback { googleSignInResult ->
                val googleSignInAccount =
                    googleSignInResult.signInAccount
                if (googleSignInAccount != null) {
                    loginGoogle(SocialRequest(googleSignInAccount.idToken!!))
                }
            }
        }
    }
}