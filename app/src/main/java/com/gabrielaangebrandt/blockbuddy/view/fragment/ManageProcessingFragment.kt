package com.gabrielaangebrandt.blockbuddy.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.gabrielaangebrandt.blockbuddy.TAG
import com.gabrielaangebrandt.blockbuddy.databinding.FragmentManageProcessingBinding
import com.gabrielaangebrandt.blockbuddy.model.processing.ProcessState
import com.gabrielaangebrandt.blockbuddy.utils.PermissionsManager
import com.gabrielaangebrandt.blockbuddy.view.activity.MainActivity
import com.gabrielaangebrandt.blockbuddy.view.activity.PermissionAlertListener
import com.gabrielaangebrandt.blockbuddy.view.fragment.callback.PermissionsCallback
import com.gabrielaangebrandt.blockbuddy.viewmodel.ManageProcessingFragmentViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ManageProcessingFragment : Fragment(), PermissionsCallback {

    private var _binding: FragmentManageProcessingBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var permissionAlertListener: PermissionAlertListener

    private val viewModel: ManageProcessingFragmentViewModel by viewModel()
    private val permissionsManager: PermissionsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerPermissionCallback()
        permissionsManager.setListener(this)
        startObserving()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageProcessingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAction.apply {
            viewModel.setupUI()
            setOnClickListener {
                if (viewModel.isServiceRunning) {
                    onPermissionsGranted()
                } else {
                    permissionsManager.requestPermissions()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            permissionAlertListener = context as MainActivity
        } catch (castException: ClassCastException) {
            Log.e(TAG, "This activity does not implement requested listener")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // DO NOT REMOVE, binding must be cleared
        _binding = null
    }

    override fun onPermissionsGranted() {
        binding.btnAction.isClickable = true
        viewModel.changeState()
    }

    override fun onPermissionsMissing(missingPermissions: Array<String>) {
        val rationaleNeeded =
            missingPermissions.map {
                shouldShowRequestPermissionRationale(it)
            }.contains(true)

        when {
            rationaleNeeded ->
                permissionAlertListener.createPermissionAlert()
            else ->
                requestPermissionLauncher.launch(missingPermissions)
        }
    }

    private fun registerPermissionCallback() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissionsManager.requestPermissions()
            }
    }

    private fun startObserving() {
        viewModel.processState.observe(this, ::updateUI)
    }

    private fun updateUI(processState: ProcessState) {
        binding.btnAction.setBackgroundResource(processState.image)
        binding.tvInstructions.setText(processState.text)
    }
}