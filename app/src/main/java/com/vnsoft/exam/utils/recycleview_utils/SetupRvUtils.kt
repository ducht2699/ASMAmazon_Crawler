package com.vnsoft.exam.utils

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.*
import com.vnsoft.exam.utils.recycleview_utils.CenterLayoutManager
import com.vnsoft.exam.utils.recycleview_utils.GridSpacingItemDecoration
import com.vnsoft.exam.utils.recycleview_utils.SpeedyLinearLayoutManager
import com.vnsoft.exam.utils.recycleview_utils.StartSnapHelper


fun setupLinearLayoutRecyclerView(
    context: Context?,
    recyclerView: RecyclerView
) {
    val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
    recyclerView.layoutManager = mLayoutManager
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.setHasFixedSize(true)
    recyclerView.isNestedScrollingEnabled = false
}

fun setupSpeedyLinearLayoutRecyclerView(
    context: Context?,
    recyclerView: RecyclerView
) {
    val mLayoutManager: RecyclerView.LayoutManager =
        SpeedyLinearLayoutManager(
            context,
            SpeedyLinearLayoutManager.VERTICAL,
            false
        )
    recyclerView.layoutManager = mLayoutManager
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.setHasFixedSize(true)
    recyclerView.isNestedScrollingEnabled = false
}

fun setupLinearLayoutHorizontalRecyclerView(
    context: Context?,
    recyclerView: RecyclerView
) {
    // setLayoutManager
    val mLayoutManager: RecyclerView.LayoutManager =
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    recyclerView.layoutManager = mLayoutManager
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.setHasFixedSize(true)
    //        recyclerView.setNestedScrollingEnabled(false);
    val startSnapHelper: SnapHelper =
        StartSnapHelper()
    recyclerView.onFlingListener = null
    startSnapHelper.attachToRecyclerView(recyclerView)
}

fun setupLinearLayoutRecyclerView1(
    context: Context?,
    recyclerView: RecyclerView
) {
    // setLayoutManager
    val mLayoutManager =
        CenterLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    recyclerView.layoutManager = mLayoutManager
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.setHasFixedSize(true)
    recyclerView.setNestedScrollingEnabled(false);
}

fun setupLinearLayoutWithDividerRecyclerView(
    context: Context?,
    recyclerView: RecyclerView
) {
    // setLayoutManager
    val llm = LinearLayoutManager(context)
    llm.orientation = LinearLayoutManager.VERTICAL
    val DividerItemDecoration = DividerItemDecoration(
        recyclerView.context,
        llm.orientation
    )
    recyclerView.addItemDecoration(DividerItemDecoration)
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.setHasFixedSize(true)
    recyclerView.isNestedScrollingEnabled = false
    recyclerView.layoutManager = llm
}

fun setupStaggeredGridRecyclerView(recyclerView: RecyclerView, column: Int) {
    val mLayoutManager =
        StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.VERTICAL)
    recyclerView.layoutManager = mLayoutManager
    recyclerView.setItemViewCacheSize(20)
    recyclerView.isDrawingCacheEnabled = true
    recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
}

fun setupGridLayoutRecyclerView(
    context: Context?,
    recyclerView: RecyclerView,
    column: Int,
    spacingInPixels: Int
) {
    val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, column)
    recyclerView.layoutManager = mLayoutManager
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.setHasFixedSize(true)
    recyclerView.addItemDecoration(
        GridSpacingItemDecoration(
            2,
            spacingInPixels,
            true,
            0
        )
    )
}