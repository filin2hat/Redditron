package com.biryulindevelop.redditron.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.biryulindevelop.redditron.R
import com.biryulindevelop.redditron.data.api.ONBOARDING_IS_SHOWN
import com.biryulindevelop.redditron.domain.storageservice.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val storageService: StorageService) :
    ViewModel() {

    fun saveOnboardingShown() {
        storageService.save(ONBOARDING_IS_SHOWN, true)
    }

    fun navigateToAuth(fragment: Fragment) {
        fragment.findNavController().navigate(R.id.action_navigation_onboarding_to_navigation_auth)
    }
}