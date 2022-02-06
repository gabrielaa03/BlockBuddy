package com.gabrielaangebrandt.blockbuddy.view.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gabrielaangebrandt.blockbuddy.databinding.LayoutHistoryItemBinding
import com.gabrielaangebrandt.blockbuddy.model.processing.CallLogModel

class HistoryListAdapter : RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder>() {

    private val callLogs: MutableList<CallLogModel> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutHistoryItemBinding.inflate(inflater, parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HistoryViewHolder,
        position: Int
    ) {
        holder.bind(callLogs[position])
    }

    override fun getItemCount(): Int = callLogs.size

    fun setListItems(newCalLLogs: List<CallLogModel>) {
        val diffCallback = DiffUtilCallback(newCalLLogs, callLogs)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        callLogs.clear()
        callLogs.addAll(newCalLLogs)
    }

    inner class HistoryViewHolder(
        private val binding: LayoutHistoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CallLogModel) {
            with(binding) {
                ivIcon.setColorFilter(
                    ContextCompat.getColor(
                        itemView.context,
                        item.state.color
                    )
                )
                tvCaller.text = item.name
                tvNumber.text = item.number
                tvTime.text = item.time
            }
        }
    }
}