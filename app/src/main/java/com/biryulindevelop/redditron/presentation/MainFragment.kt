package com.biryulindevelop.redditron.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.biryulindevelop.common.constants.ONBOARDING_SHOWN
import com.biryulindevelop.common.constants.TOKEN_ENABLED
import com.biryulindevelop.domain.storageservice.StorageService
import com.biryulindevelop.redditron.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    @Inject
    lateinit var storageService: StorageService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavigation()
    }

    private fun setNavigation() {
        val toOnboardingFragment = MainFragmentDirections.actionMainFragmentToNavigationOnboarding()
        val toAuthFragment = MainFragmentDirections.actionMainFragmentToAuthFragment()
        val toHomeFragment = MainFragmentDirections.actionMainFragmentToNavigationHome()

        val navController = findNavController()
        val shouldNavigateToHome = storageService.load(TOKEN_ENABLED)
        val shouldNavigateToAuth = storageService.load(ONBOARDING_SHOWN)

        val destination = when {
            shouldNavigateToHome -> toHomeFragment
            shouldNavigateToAuth -> toAuthFragment
            else -> toOnboardingFragment
        }
        navController.navigate(destination)
    }
}