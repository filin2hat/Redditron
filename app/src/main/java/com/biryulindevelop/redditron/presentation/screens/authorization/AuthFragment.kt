package com.biryulindevelop.redditron.presentation.screens.authorization

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.redditron.R
import com.biryulindevelop.redditron.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/** binding is based on library "ViewBindingPropertyDelegate", by Kirill Rozov aka kirich1409
more info:  https://github.com/androidbroadcast/ViewBindingPropertyDelegate */

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {
    private val binding by viewBinding(FragmentAuthBinding::bind)
    private val viewModel: AuthViewModel by viewModels()
    private val args: AuthFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAuthorizationButton()
        updateUiOnLoadStateChange()
        viewModel.createToken(args.code)
    }

    private fun setAuthorizationButton() {
        binding.authButton.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(com.biryulindevelop.common.constants.REQUEST))
            startActivity(intent)
        }
    }

    private fun updateUiOnLoadStateChange() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    LoadState.NotStartedYet ->
                        setViewsStates(
                            buttonIsEnabled = true,
                            textIsVisible = false,
                            progressIsVisible = false
                        )

                    LoadState.Loading ->
                        setViewsStates(
                            buttonIsEnabled = false,
                            textIsVisible = false,
                            progressIsVisible = true
                        )

                    is LoadState.Content -> {
                        setViewsStates(
                            buttonIsEnabled = false,
                            textIsVisible = true,
                            progressIsVisible = false
                        )
                        findNavController().navigate(R.id.action_navigation_auth_to_navigation_home)
                    }

                    is LoadState.Error -> {
                        setViewsStates(
                            buttonIsEnabled = true,
                            textIsVisible = true,
                            progressIsVisible = false
                        )
                        binding.text.text = state.message
                        Log.e(TAG, "loading error: ${state.message}")
                    }
                }
            }
        }
    }

    private fun setViewsStates(
        buttonIsEnabled: Boolean,
        textIsVisible: Boolean,
        progressIsVisible: Boolean
    ) {
        binding.authButton.isEnabled = buttonIsEnabled
        binding.text.isVisible = textIsVisible
        binding.progressBar.isVisible = progressIsVisible
    }
}
