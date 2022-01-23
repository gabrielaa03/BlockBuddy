package com.gabrielaangebrandt.blockbuddy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gabrielaangebrandt.blockbuddy.databinding.FragmentMainBinding
import com.gabrielaangebrandt.blockbuddy.model.ProcessState
import com.gabrielaangebrandt.blockbuddy.viewmodel.MainFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserving()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAction.apply {
            viewModel.setupUI()
            setOnClickListener { viewModel.changeState() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun startObserving() {
        viewModel.processState.observe(this, ::updateUI)
    }

    private fun updateUI(processState: ProcessState) {
        binding.btnAction.setBackgroundResource(processState.image)
        binding.tvInstructions.setText(processState.text)
    }
}