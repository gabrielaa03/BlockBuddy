package com.gabrielaangebrandt.blockbuddy.view.activity

import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.broadcastreceiver.CallListener
import com.gabrielaangebrandt.blockbuddy.broadcastreceiver.PhoneStateReceiver
import com.gabrielaangebrandt.blockbuddy.databinding.ActivityMainBinding
import com.gabrielaangebrandt.blockbuddy.viewmodel.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), CallListener {

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

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
        val navigationController = navHostFragment.navController

        val bottomNavigationBar = binding.navViewLayout.bottomNavView
        bottomNavigationBar.setupWithNavController(navigationController)
    }
}
