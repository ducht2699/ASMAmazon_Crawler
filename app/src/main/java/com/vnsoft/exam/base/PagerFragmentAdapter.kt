package com.vnsoft.exam.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import java.util.*

class PagerFragmentAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList: MutableList<Fragment> =
        ArrayList()
    private val fragmentTitleList: MutableList<String> =
        ArrayList()


    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        fragmentTitleList.add(fragment.javaClass.simpleName)
    }

    fun addFragment(
        fragment: Fragment,
        title: String
    ) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): String {
        return fragmentTitleList[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}