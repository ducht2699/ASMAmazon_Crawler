package com.vnsoft.asm_crawler.ui.main

import android.annotation.SuppressLint
import com.vnsoft.asm_crawler.data.DataManager
import com.vnsoft.asm_crawler.network.Api
import com.vnsoft.asm_crawler.view_model.BaseViewModel
import javax.inject.Inject


@SuppressLint("CheckResult")
class MainViewModel(): BaseViewModel() {
    @Inject
    lateinit var dataManager: DataManager
    @Inject
    lateinit var api: Api

}