package com.gabrielaangebrandt.blockbuddy.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.databinding.FragmentSettingsBinding
import com.gabrielaangebrandt.blockbuddy.model.viewrendering.SettingsFragmentData
import com.gabrielaangebrandt.blockbuddy.utils.hideKeyboard
import com.gabrielaangebrandt.blockbuddy.utils.subscribe
import com.gabrielaangebrandt.blockbuddy.view.fragment.adapter.BlockedNumbersAdapter
import com.gabrielaangebrandt.blockbuddy.viewmodel.SettingsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val adapter: BlockedNumbersAdapter by lazy {
        BlockedNumbersAdapter()
    }

    private val viewModel: SettingsFragmentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getViewData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        viewModel.viewData.subscribe(this, ::setUpUi)
        viewModel.blockedNumbersUpdated.subscribe(this, ::onNumberAddedToList)
        viewModel.numberAlreadyBlocked.subscribe(this, ::onNumberAlreadyBlocked)
    }

    private fun setUpUi(data: SettingsFragmentData) {
        with(binding) {
            tvSettingsInstructions.text = getString(data.instructions)

            with(allowCallsFromContactsOnly) {
                tvOption.text = getString(data.allowOnlyCallsText)
                switchOption.apply {
                    isChecked = data.allowContactCallsChecked
                    setOnCheckedChangeListener { _, isChecked ->
                        viewModel.saveSettings(isChecked)
                    }
                }
            }

            with(customBlocking) {
                val layManager = LinearLayoutManager(context)
                layManager.orientation = LinearLayoutManager.VERTICAL
                recyclerViewBlockedNumbers.layoutManager = layManager
                recyclerViewBlockedNumbers.adapter = adapter
                recyclerViewBlockedNumbers.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        layManager.orientation
                    )
                )
                adapter.setNumbers(data.blockedNumbers)

                etNumber.doOnTextChanged { text, _, _, _ ->
                    if (!text.isNullOrEmpty()) {
                        enableButton(text.toString())
                    }
                }
                btnAddNumber.setOnClickListener {
                    viewModel.blockNumber(etNumber.text.toString())
                }
            }
        }
    }

    // button can be enabled only if a text in etNumber satisfy regex
    private fun enableButton(text: String) {
        binding.customBlocking.btnAddNumber.isEnabled =
            viewModel.isNumberValid(text)
    }

    private fun onNumberAddedToList(blockedNumbers: List<String>) {
        adapter.setNumbers(blockedNumbers)
        Toast.makeText(context, R.string.caller_blocked, Toast.LENGTH_LONG).show()

        binding.customBlocking.etNumber.run {
            clearFocus()
            text?.clear()
            hideKeyboard()
        }
    }

    private fun onNumberAlreadyBlocked() {
        Toast.makeText(context, R.string.this_number_is_already_blocked, Toast.LENGTH_LONG).show()
    }
}