package com.biryulindevelop.redditron.presentation.screens.onboarding

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.biryulindevelop.redditron.R
import com.biryulindevelop.redditron.databinding.FragmentOnboardingBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/** binding is based on library "ViewBindingPropertyDelegate", by Kirill Rozov aka kirich1409
more info:  https://github.com/androidbroadcast/ViewBindingPropertyDelegate */

@AndroidEntryPoint
class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private var mediator: TabLayoutMediator? = null
    private val binding by viewBinding(FragmentOnboardingBinding::bind)
    private val viewModel: OnboardingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTabs()
        setupAuthorizeButton()
        saveOnboardingShown()
    }

    private fun setupViewPager() {
        val onboardingTextsArray1 = resources.getStringArray(R.array.onboarding_texts_array1)
        val onboardingTextsArray2 = resources.getStringArray(R.array.onboarding_texts_array2)
        val onboardingImages = arrayOf(
            ResourcesCompat.getDrawable(resources, R.drawable.onb_image1, null)
                ?: error("lost onboarding image1"),
            ResourcesCompat.getDrawable(resources, R.drawable.onb_image2, null)
                ?: error("lost onboarding image2"),
            ResourcesCompat.getDrawable(resources, R.drawable.onb_image3, null)
                ?: error("lost onboarding image3")
        )
        val viewPagerAdapter =
            ViewPagerAdapter(onboardingTextsArray1, onboardingTextsArray2, onboardingImages)
        binding.viewPager.adapter = viewPagerAdapter

        val lastPageIndex = onboardingTextsArray1.lastIndex
        val changeButtonTextOnPageChange =
            ChangeButtonTextOnPageChange(binding.skipButton, requireContext(), lastPageIndex)
        binding.viewPager.registerOnPageChangeCallback(changeButtonTextOnPageChange)
    }

    private fun setupTabs() {
        mediator = TabLayoutMediator(binding.tabs, binding.viewPager) { _, _ -> }
        mediator?.attach()
    }

    private fun setupAuthorizeButton() {
        with(binding.skipButton) {
            background = null
            setOnClickListener {
                findNavController().navigate(
                    R.id.action_navigation_onboarding_to_navigation_auth
                )
            }
        }
    }

    private fun saveOnboardingShown() {
        viewModel.saveOnboardingShown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediator?.detach()
        mediator = null
    }
}