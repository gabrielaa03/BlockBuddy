package com.gabrielaangebrandt.blockbuddy.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrielaangebrandt.blockbuddy.TAG
import com.gabrielaangebrandt.blockbuddy.databinding.FragmentHistoryBinding
import com.gabrielaangebrandt.blockbuddy.model.processing.CallLogModel
import com.gabrielaangebrandt.blockbuddy.utils.PermissionsManager
import com.gabrielaangebrandt.blockbuddy.utils.subscribe
import com.gabrielaangebrandt.blockbuddy.view.activity.MainActivity
import com.gabrielaangebrandt.blockbuddy.view.activity.MainListener
import com.gabrielaangebrandt.blockbuddy.view.fragment.adapter.HistoryListAdapter
import com.gabrielaangebrandt.blockbuddy.view.fragment.callback.PermissionsCallback
import com.gabrielaangebrandt.blockbuddy.viewmodel.HistoryFragmentViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment(), PermissionsCallback {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var listener: MainListener
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private val historyAdapter: HistoryListAdapter by lazy {
        HistoryListAdapter()
    }

    private val viewModel: HistoryFragmentViewModel by viewModel()
    private val permissionsManager: PermissionsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserving()
        registerPermissionCallback()
    }

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
            recyclerViewHistory.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = historyAdapter
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as MainActivity
        } catch (castException: ClassCastException) {
            Log.e(TAG, "This activity does not implement requested listener")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        permissionsManager.setListener(this)
        permissionsManager.requestPermissions(false)
    }

    private fun startObserving() {
        viewModel.callLogs.subscribe(this, ::notifyAdapter)
    }

    private fun registerPermissionCallback() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissionsManager.requestPermissions(true)
            }
    }

    private fun notifyAdapter(callLogs: List<CallLogModel>) {
        historyAdapter.setListItems(callLogs)
        binding.progressBar.isVisible = false
    }

    override fun onPermissionsGranted(changeRequested: Boolean) {
        toggleViews(permissionsGranted = true)
        viewModel.filterCallLogs()
    }

    override fun onPermissionsMissing(missingPermissions: Array<String>) {
        toggleViews(permissionsGranted = false)

        val rationaleNeeded =
            missingPermissions.map {
                shouldShowRequestPermissionRationale(it)
            }.contains(true)

        when {
            rationaleNeeded ->
                listener.createPermissionAlert()
            else ->
                requestPermissionLauncher.launch(missingPermissions)
        }
    }

    private fun toggleViews(permissionsGranted: Boolean) {
        with(binding) {
            progressBar.isVisible = permissionsGranted
            recyclerViewHistory.isVisible = permissionsGranted
            tvAllowPermissions.isVisible = !permissionsGranted
        }
    }
}