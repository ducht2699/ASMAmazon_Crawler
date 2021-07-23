package com.vnsoft.exam.base

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vnsoft.exam.R

abstract class BaseBottomSheetDialog<BINDING : ViewDataBinding>: BottomSheetDialogFragment() {

    lateinit var binding: BINDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, getLayoutResource(), container, false)
        if (this.dialog?.window != null) {
            this.dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            this.dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        }
        initView(savedInstanceState, binding.root)
        return binding.root
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
                (activity?.window?.decorView?.width?.times(width) ?: 0 ) / 10,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener(view)
    }

    protected abstract fun getLayoutResource(): Int

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