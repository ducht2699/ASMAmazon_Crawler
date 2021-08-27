package com.vnsoft.asm_crawler.ui.main

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import com.vnsoft.asm_crawler.R
import com.vnsoft.asm_crawler.base.BaseActivity
import com.vnsoft.asm_crawler.databinding.ActivityMainBinding
import com.vnsoft.asm_crawler.view_model.ViewModelFactory


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
