package com.gabrielaangebrandt.blockbuddy.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrielaangebrandt.blockbuddy.databinding.FragmentHistoryBinding
import com.gabrielaangebrandt.blockbuddy.view.fragment.adapter.HistoryListAdapter
import com.gabrielaangebrandt.blockbuddy.viewmodel.HistoryFragmentViewModel
import org.koin.android.ext.android.inject

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyAdapter: HistoryListAdapter by lazy {
        HistoryListAdapter()
    }
    private val viewModel: HistoryFragmentViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = historyAdapter
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        historyAdapter.setListItems(viewModel.filterCallLogs())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}