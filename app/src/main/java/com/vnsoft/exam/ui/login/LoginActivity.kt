package com.vnsoft.exam.ui.login

import android.content.Context
import android.content.Intent
import android.text.Html
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.vnsoft.exam.R
import com.vnsoft.exam.base.BaseActivity
import com.vnsoft.exam.databinding.ActivityLoginBinding
import com.vnsoft.exam.model.request.LoginRequest
import com.vnsoft.exam.model.request.SocialRequest
import com.vnsoft.exam.ui.main.MainActivity
import com.vnsoft.exam.utils.GsonUtils
import com.vnsoft.exam.view_model.ViewModelFactory
import java.util.*

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    private val RC_SIGN_IN = 1000
    private val TAG: String = "LoginActivity"
    private var fbLoginManager: LoginManager? = null
    private var callbackManager: CallbackManager? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null


    companion object {
        fun getIntent(
            context: Context
        ): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            return intent
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_login
    }

    override fun observerLiveData() {
        viewModel.apply {
            loginResponse.observe(this@LoginActivity, androidx.lifecycle.Observer { data ->
                startActivity(MainActivity.getIntent(this@LoginActivity))
                finishAffinity();
            })
        }
    }

    override fun initView() {
        binding.tvRegister.text = Html.fromHtml(resources.getString(R.string.str_has_account))

        //init facebook
        fbLoginManager = LoginManager.getInstance()
        callbackManager = CallbackManager.Factory.create()

        initGoogle()
    }

    override fun initListener() {
        binding.btnLogin.setOnClickListener {
            viewModel.login(
                LoginRequest(
                    binding.edtPhone.text.toString(),
                    binding.edtPassword.text.toString()
                )
            )
        }

        binding.tvRegister.setOnClickListener {
        }

        binding.ivFacebook.setOnClickListener {
            fbLoginManager?.logInWithReadPermissions(this, Arrays.asList("email", "public_profile"))
        }

        getInfoFacebookUser()

        binding.ivGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient?.signInIntent
            startActivityForResult(signInIntent,RC_SIGN_IN)
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(LoginViewModel::class.java)
    }

    private fun getInfoFacebookUser() {
        fbLoginManager?.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                println("onSuccess")
                val accessToken = loginResult.accessToken.token
                Log.i(TAG, "accessToken:$accessToken")
                viewModel.loginFacebook(SocialRequest(accessToken))
            }

            override fun onCancel() {
                println("onCancel")
            }

            override fun onError(error: FacebookException) {
                println("onError")
            }
        })
    }

    private fun initGoogle() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleSignInClient?.signOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            Log.d(TAG, "handleSignInResult: " + GsonUtils.getInstance().toJson(account))
            // Signed in successfully, show authenticated UI.
            viewModel.loginGoogle(SocialRequest(account?.idToken!!))
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
}
