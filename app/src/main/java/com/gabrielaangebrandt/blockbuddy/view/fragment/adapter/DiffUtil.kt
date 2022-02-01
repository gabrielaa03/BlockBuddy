package com.gabrielaangebrandt.blockbuddy.view.fragment.adapter

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback(
    private val oldData: List<Any>,
    private val newData: List<Any>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = oldData[oldItemPosition] == newData[newItemPosition]

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = oldData[oldItemPosition] == newData[newItemPosition]
}
