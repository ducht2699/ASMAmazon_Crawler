package com.vnsoft.exam.ui.main

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import com.vnsoft.exam.R
import com.vnsoft.exam.base.BaseActivity
import com.vnsoft.exam.databinding.ActivityMainBinding
import com.vnsoft.exam.view_model.ViewModelFactory


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    val TAG = "MainActivity"

    companion object {
        fun getIntent(
            context: Context
        ): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_main
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, ViewModelFactory(this)).get(MainViewModel::class.java)
    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun observerLiveData() {
    }
}
