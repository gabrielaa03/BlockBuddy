package com.gabrielaangebrandt.blockbuddy.view.activity

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.broadcastreceiver.CallListener
import com.gabrielaangebrandt.blockbuddy.broadcastreceiver.PhoneStateReceiver
import com.gabrielaangebrandt.blockbuddy.databinding.ActivityMainBinding
import com.gabrielaangebrandt.blockbuddy.viewmodel.MainActivityViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), CallListener, PermissionAlertListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val stateReceiver: PhoneStateReceiver by lazy {
        PhoneStateReceiver()
    }
    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BlockBuddy_NoActionBar)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarLayout.toolbar)

        setUpNavigation()
    }

    override fun onResume() {
        super.onResume()
        stateReceiver.setListener(this)
        registerReceiver(stateReceiver, IntentFilter(""))
    }

    override fun onCallReceived(number: String) {
        viewModel.processCall(number)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(stateReceiver)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.hostFragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun createPermissionAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.permission_needed)
            .setMessage(R.string.blockbuddy_needs_your_permission)
            .setPositiveButton(R.string.allow) { _, _ -> navigateToAppSettings() }
            .setNegativeButton(R.string.cancel) { view, _ -> view.dismiss() }
            .create()
            .show()
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
        val navigationController = navHostFragment.navController

        val bottomNavigationBar = binding.navViewLayout.bottomNavView
        bottomNavigationBar.setupWithNavController(navigationController)
    }

    private fun navigateToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.fromParts(PACKAGE_SCHEME, packageName, null)
        }
        startActivity(intent)
    }

    companion object {
        private const val PACKAGE_SCHEME = "package"
    }
}
