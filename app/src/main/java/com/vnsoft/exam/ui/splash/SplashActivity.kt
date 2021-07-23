package com.vnsoft.exam.ui.splash

import androidx.lifecycle.ViewModelProviders
import com.vnsoft.exam.R
import com.vnsoft.exam.base.BaseActivity
import com.vnsoft.exam.databinding.ActivitySplashBinding
import com.vnsoft.exam.ui.login.LoginActivity
import com.vnsoft.exam.ui.login.LoginViewModel
import com.vnsoft.exam.ui.main.MainActivity
import com.vnsoft.exam.view_model.ViewModelFactory

class SplashActivity : BaseActivity<LoginViewModel, ActivitySplashBinding>() {

    override fun getContentLayout(): Int {
        return R.layout.activity_splash
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(LoginViewModel::class.java)
    }

    override fun initView() {
    }

    override fun initListener() {
        viewModel.autoLogin(this)
    }

    override fun observerLiveData() {
        viewModel.apply {
            loginResponse.observe(this@SplashActivity, {
                startActivity(MainActivity.getIntent(this@SplashActivity))
                finishAffinity()
            })
            responseMessage.observe(this@SplashActivity, {
                startActivity(LoginActivity.getIntent(this@SplashActivity))
                finishAffinity()
            })
        }
    }
}
