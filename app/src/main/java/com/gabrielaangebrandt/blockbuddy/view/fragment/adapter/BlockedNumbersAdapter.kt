package com.gabrielaangebrandt.blockbuddy.view.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gabrielaangebrandt.blockbuddy.databinding.LayoutBlockedNumberItemBinding

class BlockedNumbersAdapter :
    RecyclerView.Adapter<BlockedNumbersAdapter.BlockedNumbersViewHolder>() {

    private var _binding: LayoutBlockedNumberItemBinding? = null
    private val binding get() = _binding!!
    private val numbers: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockedNumbersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        _binding = LayoutBlockedNumberItemBinding.inflate(inflater, parent, false)
        return BlockedNumbersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlockedNumbersViewHolder, position: Int) {
        holder.bind(numbers[position])
    }

    override fun getItemCount(): Int = numbers.size

    fun setNumbers(newNumbers: List<String>) {
        val diffCallback = DiffUtilCallback(numbers, newNumbers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        numbers.clear()
        numbers.addAll(newNumbers)
    }

    inner class BlockedNumbersViewHolder(
        private val binding: LayoutBlockedNumberItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(number: String) {
            binding.tvNumber.text = number
        }
    }
}