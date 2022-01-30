package com.gabrielaangebrandt.blockbuddy.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.databinding.FragmentMainBinding
import com.gabrielaangebrandt.blockbuddy.model.ProcessState
import com.gabrielaangebrandt.blockbuddy.utils.PermissionsManager
import com.gabrielaangebrandt.blockbuddy.viewmodel.MainFragmentViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(), PermissionsCallback {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityContext: Context
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    private val viewModel: MainFragmentViewModel by viewModel()
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
        _binding = FragmentMainBinding.inflate(inflater, container, false)
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
        activityContext = context
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
            rationaleNeeded -> {
                createPermissionAlert()
            }
            else -> {
                requestPermissionLauncher.launch(missingPermissions)
            }
        }
    }

    private fun registerPermissionCallback() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissionsManager.requestPermissions()
            }
    }

    private fun createPermissionAlert() {
        AlertDialog.Builder(activityContext)
            .setTitle(R.string.permission_needed)
            .setMessage(R.string.blockbuddy_needs_your_permission)
            .setPositiveButton(R.string.allow) { _, _ -> navigateToAppSettings() }
            .setNegativeButton(R.string.cancel) { view, _ -> view.dismiss() }
            .create()
            .show()
    }

    private fun navigateToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.fromParts(PACKAGE_SCHEME, context?.packageName, null)
        }
        startActivity(intent)
    }

    private fun startObserving() {
        viewModel.processState.observe(this, ::updateUI)
    }

    private fun updateUI(processState: ProcessState) {
        binding.btnAction.setBackgroundResource(processState.image)
        binding.tvInstructions.setText(processState.text)
    }

    companion object {
        private const val PACKAGE_SCHEME = "package"
    }
}

interface PermissionsCallback {
    fun onPermissionsGranted()
    fun onPermissionsMissing(missingPermissions: Array<String>)
}