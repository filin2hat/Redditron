package com.biryulindevelop.redditron.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.biryulindevelop.redditron.R
import com.biryulindevelop.redditron.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {
        val navView = binding.bottomNavigationView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateUIBasedOnDestination(destination.id, navView)
        }
    }

    private fun updateUIBasedOnDestination(destinationId: Int, navView: BottomNavigationView) {
        if (destinationId == R.id.navigation_onboarding || destinationId == R.id.navigation_auth) {
            navView.visibility = View.GONE
        } else {
            navView.visibility = View.VISIBLE
        }
    }
}