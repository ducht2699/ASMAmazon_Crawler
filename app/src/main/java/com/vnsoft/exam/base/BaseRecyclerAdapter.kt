package com.vnsoft.exam.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T, V : RecyclerView.ViewHolder?>(dataSet: MutableList<T?>?) :
    RecyclerView.Adapter<V>() {
    private var dataSet: MutableList<T?>?

    protected abstract fun getLayoutResourceItem(): Int

    abstract fun onCreateBasicViewHolder(binding: ViewDataBinding?): V
    abstract fun onBindBasicItemView(holder: V, position: Int)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            layoutInflater,
            getLayoutResourceItem(),
            parent,
            false
        ) as ViewDataBinding
        return onCreateBasicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        onBindBasicItemView(holder, position)
    }

    override fun getItemCount(): Int {
        return if (dataSet == null) 0 else dataSet!!.size
    }

    fun addItem(item: T) {
        if (!dataSet!!.contains(item)) {
            dataSet!!.add(item)
            notifyItemInserted(dataSet!!.size - 1)
        }
    }

    fun addItems(newDataSetItems: MutableList<T?>) {
        if (newDataSetItems.size == 0) return
        val positionStart = itemCount
        val itemCount = newDataSetItems.size
        dataSet!!.addAll(newDataSetItems)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    fun removeItem(item: T) {
        val indexOfItem = dataSet!!.indexOf(item)
        removeItem(indexOfItem)
    }

    fun removeItem(indexOfItem: Int) {
        if (indexOfItem != -1) {
            dataSet!!.removeAt(indexOfItem)
            notifyItemRemoved(indexOfItem)
        }
    }

    fun getItem(index: Int): T? {
        return if (dataSet != null && dataSet!![index] != null) {
            dataSet!![index]
        } else {
            throw IllegalArgumentException("Item with index $index doesn't exist, dataSet is $dataSet")
        }
    }

    fun getDataSet(): List<T?>? {
        return if (dataSet != null) {
            dataSet
        } else null
    }

    fun setDataSet(newDataSet: MutableList<T?>) {
        dataSet = newDataSet
        notifyDataSetChanged()
    }

    fun resetItems(newDataSet: MutableList<T?>) {
        dataSet!!.clear()
        addItems(newDataSet)
    }

    fun clear() {
        dataSet!!.clear()
        notifyDataSetChanged()
    }

    val isEmpty: Boolean
        get() = itemCount == 0

    fun isValidPos(position: Int): Boolean {
        return position >= 0 && position < itemCount
    }

    init {
        this.dataSet = dataSet
    }
}