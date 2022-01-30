package com.gabrielaangebrandt.blockbuddy.view.fragment.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gabrielaangebrandt.blockbuddy.databinding.LayoutHistoryItemBinding
import com.gabrielaangebrandt.blockbuddy.model.CallLog

class HistoryListAdapter : RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder>() {

    private val data: MutableList<CallLog> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutHistoryItemBinding.inflate(inflater, parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun setListItems(items: List<CallLog>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(
        private val binding: LayoutHistoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CallLog) {
            with(binding) {
                ivIcon.setColorFilter(
                    ContextCompat.getColor(itemView.context, item.type.color)
                )
                tvCaller.text = item.name
                tvTime.text = item.time
            }
        }
    }
}