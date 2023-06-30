package com.biryulindevelop.redditron.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.biryulindevelop.redditron.application.ONBOARDING_SHOWN
import com.biryulindevelop.redditron.application.TOKEN_ENABLED
import com.biryulindevelop.redditron.domain.storageservice.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val storageService: StorageService) :
    ViewModel() {

    fun setNavigation(fragment: Fragment) {

        val toOnboardingFragment = MainFragmentDirections.actionMainFragmentToNavigationOnboarding()
        val toAuthFragment = MainFragmentDirections.actionMainFragmentToAuthFragment()
        val toHomeFragment = MainFragmentDirections.actionMainFragmentToNavigationHome()

        if (storageService.load(TOKEN_ENABLED))
            fragment.findNavController().navigate(toHomeFragment)
        else {
            if (storageService.load(ONBOARDING_SHOWN))
                fragment.findNavController().navigate(toAuthFragment)
            else fragment.findNavController().navigate(toOnboardingFragment)
        }
    }
}