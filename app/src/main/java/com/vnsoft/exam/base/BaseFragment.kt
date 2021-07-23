package com.vnsoft.exam.base

import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.vnsoft.exam.R
import com.vnsoft.exam.utils.CommonUtils
import com.vnsoft.exam.view_model.BaseViewModel

abstract class BaseFragment<VM : BaseViewModel, BINDING : ViewDataBinding> :
    Fragment() {

    lateinit var viewModel: VM

    lateinit var binding: BINDING
    var loadingDialog: AlertDialog? = null
    private val DOUBLE_PRESS_INTERVAL: Long = 1000
    private var mLastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = setupProgressDialog();
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, getContentLayout(), container, false
        )
        return binding.root
    }

    open fun isDoubleClick(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < DOUBLE_PRESS_INTERVAL) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
        initListener()
        observerLiveData()
        observerDefaultLiveData()
    }

    abstract fun getContentLayout(): Int

    abstract fun initViewModel()

    abstract fun initView()

    abstract fun initListener()

    abstract fun observerLiveData()

    private fun observerDefaultLiveData() {
        viewModel.apply {
            activity?.let {
                isLoading.observe(it, {
                    if (it) {
                        loadingDialog?.show()
                    } else {
                        loadingDialog?.dismiss()
                    }
                })
            }
            activity?.let {
                errorMessage.observe(it, {
                    if (it != null) {
                        showError(it.toInt())
                    }
                })
            }
            activity?.let {
                responseMessage.observe(it, {
                    showError(it.toString())
                })
            }
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
        if (context != null) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context!!, R.style.CustomDialog)
            builder.setCancelable(false)

            val myLayout = LayoutInflater.from(context!!)
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
        return null;
    }

    protected fun paddingStatusBar(view: View) {
        CommonUtils.setStatusColorIcon(view)
        view.setPadding(0, CommonUtils.getStatusBarHeight(context!!), 0, 0)
    }
}
