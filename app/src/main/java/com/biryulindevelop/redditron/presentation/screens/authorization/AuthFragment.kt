package com.biryulindevelop.redditron.presentation.screens.authorization

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.biryulindevelop.redditron.R
import com.biryulindevelop.common.constants.REQUEST
import com.biryulindevelop.redditron.databinding.FragmentAuthBinding
import com.biryulindevelop.redditron.domain.state.LoadState
import com.biryulindevelop.redditron.presentation.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding>() {

    override fun initBinding(inflater: LayoutInflater) = FragmentAuthBinding.inflate(inflater)
    private val viewModel by viewModels<AuthViewModel>()
    private val args by navArgs<AuthFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAuthorizationButton()
        updateUiOnLoadStateChange()
        viewModel.createToken(args.code)
    }

    private fun setAuthorizationButton() {
        binding.authButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(com.biryulindevelop.common.constants.REQUEST))
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
