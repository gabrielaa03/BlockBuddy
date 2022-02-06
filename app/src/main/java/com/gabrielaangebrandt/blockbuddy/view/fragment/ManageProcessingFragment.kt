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
import com.gabrielaangebrandt.blockbuddy.view.activity.MainListener
import com.gabrielaangebrandt.blockbuddy.view.fragment.callback.PermissionsCallback
import com.gabrielaangebrandt.blockbuddy.viewmodel.ManageProcessingFragmentViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ManageProcessingFragment : Fragment(), PermissionsCallback {

    private var _binding: FragmentManageProcessingBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var listener: MainListener

    private val viewModel: ManageProcessingFragmentViewModel by viewModel()
    private val permissionsManager: PermissionsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerPermissionCallback()
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
            setOnClickListener { checkServiceState(true) }
        }
    }

    override fun onResume() {
        super.onResume()
        checkServiceState()
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
        // DO NOT REMOVE, binding must be cleared
        _binding = null
    }

    override fun onPermissionsGranted(changeRequested: Boolean) {
        binding.btnAction.isClickable = true

        if (changeRequested) {
            viewModel.changeServiceState()

            if (viewModel.isServiceRunning) {
                listener.startProcessingService()
            } else {
                listener.stopProcessingService()
            }
        }
    }

    override fun onPermissionsMissing(missingPermissions: Array<String>) {
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

    private fun checkServiceState(changeRequested: Boolean = false) {
        // if service is already running, skip requesting permissions
        if (viewModel.isServiceRunning) {
            onPermissionsGranted(changeRequested)
        } else {
            permissionsManager.setListener(this)
            permissionsManager.requestPermissions(changeRequested)
        }
    }

    private fun registerPermissionCallback() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissionsManager.requestPermissions(false)
            }
    }

    private fun startObserving() {
        viewModel.processState.observe(this, ::updateUI)
    }

    private fun updateUI(processState: ProcessState) {
        with(binding) {
            with(btnAction) {
                alpha = 0f
                translationY = 50F
                this.animate()
                    .alpha(1f)
                    .translationYBy(-50F)
                    .duration = 1000
                setBackgroundResource(processState.image)
            }
            tvProcessingState.setText(processState.processingState)
            tvInstructions.setText(processState.instructionsText)
        }
    }
}