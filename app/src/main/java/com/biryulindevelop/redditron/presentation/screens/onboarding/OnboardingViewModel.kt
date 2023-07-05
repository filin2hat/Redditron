package com.biryulindevelop.redditron.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import com.biryulindevelop.common.constants.ONBOARDING_SHOWN
import com.biryulindevelop.domain.storageservice.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {

    fun saveOnboardingShown() {
        storageService.save(ONBOARDING_SHOWN, true)
    }
}