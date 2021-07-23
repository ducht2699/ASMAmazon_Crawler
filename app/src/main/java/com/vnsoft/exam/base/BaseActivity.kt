package com.vnsoft.exam.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.vnsoft.exam.R
import com.vnsoft.exam.view_model.BaseViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper


abstract class BaseActivity<VM : BaseViewModel, BINDING : ViewDataBinding> :
    AppCompatActivity() {

    lateinit var viewModel: VM
    lateinit var binding: BINDING
    var loadingDialog: AlertDialog? = null

    private val DOUBLE_PRESS_INTERVAL: Long = 1000
    private var mLastClickTime: Long = 0

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = DataBindingUtil.setContentView(this, getContentLayout())
        setContentView(binding.root)
        loadingDialog = setupProgressDialog();
        initViewModel()
        observerDefaultLiveData()
        initView()
        initListener()
        observerLiveData()
    }

    abstract fun getContentLayout(): Int

    abstract fun initViewModel()

    abstract fun initView()

    abstract fun initListener()

    abstract fun observerLiveData()

    open fun isDoubleClick(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < DOUBLE_PRESS_INTERVAL) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }

    private fun observerDefaultLiveData() {
        viewModel.apply {
            isLoading.observe(this@BaseActivity, {
                if (it) {
                    loadingDialog?.show()
                } else {
                    loadingDialog?.dismiss()
                }
            })
            errorMessage.observe(this@BaseActivity, {
                if (it != null) {
                    showError(it.toInt())
                }
            })
            responseMessage.observe(this@BaseActivity, {
                showError(it.toString())
            })
        }

    }

    private fun showError(errorMessage: String) {
        val errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG)
        errorSnackbar.setAction("", null)
        errorSnackbar.show()
    }

    private fun showError(@StringRes id: Int) {
        val errorSnackbar = Snackbar.make(binding.root, id, Snackbar.LENGTH_LONG)
        errorSnackbar.setAction("", null)
        errorSnackbar.show()
    }

    private fun setupProgressDialog() : AlertDialog? {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.CustomDialog)
        builder.setCancelable(false)

        val myLayout = LayoutInflater.from(this)
        val dialogView: View = myLayout.inflate(R.layout.fragment_progress_dialog, null)

        builder.setView(dialogView)

        val dialog: AlertDialog = builder.create()
        val window: Window? = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }
}
