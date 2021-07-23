package com.vnsoft.exam.base

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vnsoft.exam.R
import com.vnsoft.exam.utils.CommonUtils
import com.vnsoft.exam.view_model.BaseViewModel

abstract class BaseBottomSheetFullScreenDialog<VM : BaseViewModel, BINDING : ViewDataBinding>: BottomSheetDialogFragment() {

    lateinit var viewModel: VM
    lateinit var binding: BINDING

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, getLayoutResource(), container, false)
        if (this.dialog?.window != null) {
            this.dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            this.dialog?.window?.setBackgroundDrawableResource(R.color.colorBlack)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
        }
        initViewModel()
        initView(savedInstanceState, binding.root)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet?.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight - CommonUtils.getStatusBarHeight(context!!)
        }
        bottomSheet?.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        val display = activity?.display
        display?.getRealMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onStart() {
        super.onStart()
    }

    /**
     * Set size for base dialog
     */
    private fun setSizeForDialog(width: Int) {
        if (dialog?.window != null) {
            dialog?.window?.setLayout(
                (activity?.window?.decorView?.width?.times(width) ?: 0) / 10,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener(view)
    }

    protected abstract fun getLayoutResource(): Int

    protected abstract fun initViewModel()

    protected abstract fun initView(saveInstanceState: Bundle?, view: View?)

    protected abstract fun initListener(view: View?)

    override fun show(
        fragmentManager: FragmentManager,
        tag: String?
    ) {
        val transaction =
            fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

    fun dismissDialog(tag: String?) {
        dismiss()
    }
}