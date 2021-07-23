package com.vnsoft.exam.ui.main

import android.annotation.SuppressLint
import com.vnsoft.exam.data.DataManager
import com.vnsoft.exam.network.Api
import com.vnsoft.exam.view_model.BaseViewModel
import javax.inject.Inject


@SuppressLint("CheckResult")
class MainViewModel(): BaseViewModel() {
    @Inject
    lateinit var dataManager: DataManager
    @Inject
    lateinit var api: Api

}