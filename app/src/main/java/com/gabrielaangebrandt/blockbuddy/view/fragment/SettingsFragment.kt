package com.gabrielaangebrandt.blockbuddy.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.databinding.FragmentSettingsBinding
import com.gabrielaangebrandt.blockbuddy.utils.hideKeyboard
import com.gabrielaangebrandt.blockbuddy.view.fragment.adapter.BlockedNumbersAdapter
import com.gabrielaangebrandt.blockbuddy.viewmodel.Setting
import com.gabrielaangebrandt.blockbuddy.viewmodel.SettingsFragmentViewModel
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val adapter: BlockedNumbersAdapter by lazy {
        BlockedNumbersAdapter()
    }
    private val viewModel: SettingsFragmentViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // TODO: Get data from db
        adapter.setNumbers(listOf("111-222-333", "2333-333-222"))
    }

    private fun setUpUi() {
        with(binding) {
            tvSettingsInstructions.text = getString(R.string.customize_your_settings)

            with(allowCallsFromContactsOnly) {
                tvOption.text = getString(R.string.allow_calls_from_contact_list_only)
                switchOption.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.saveSettings(
                        Setting.ALLOW_CONTACTS_ONLY_CALL,
                        isChecked
                    )
                }
            }

            with(allowSmsFromContactsOnly) {
                tvOption.text = getString(R.string.allow_sms_from_contact_list_only)
                switchOption.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.saveSettings(
                        Setting.ALLOW_CONTACTS_ONLY_SMS,
                        isChecked
                    )
                }
            }

            with(customBlocking) {
                val layManager = LinearLayoutManager(context)
                recyclerViewBlockedNumbers.layoutManager = layManager
                recyclerViewBlockedNumbers.adapter = adapter
                recyclerViewBlockedNumbers.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        layManager.orientation
                    )
                )

                etNumber.doOnTextChanged { text, _, _, _ ->
                    if (!text.isNullOrEmpty()) {
                        enableButton(text.toString())
                    }
                }
                btnAddNumber.setOnClickListener {
                    // TODO: Clear once number is saved
                    it.hideKeyboard()
                    etNumber.run {
                        clearFocus()
                        text?.clear()
                    }
                    viewModel.blockNumber(etNumber.text.toString())
                }
            }
        }
    }

    private fun enableButton(text: String) {
        binding.customBlocking.btnAddNumber.isEnabled =
            viewModel.isNumberValid(text)
    }
}