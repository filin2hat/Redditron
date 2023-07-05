package com.biryulindevelop.redditron.presentation.screens.profile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.biryulindevelop.domain.models.Profile
import com.biryulindevelop.domain.state.LoadState
import com.biryulindevelop.redditron.R
import com.biryulindevelop.redditron.databinding.FragmentProfileBinding
import com.biryulindevelop.redditron.presentation.utils.loadImage
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/** binding is based on library "ViewBindingPropertyDelegate", by Kirill Rozov aka kirich1409
more info:  https://github.com/androidbroadcast/ViewBindingPropertyDelegate */

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        with(binding) {
            buttonListOfFriends.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_profile_to_navigation_friends)
            }
            buttonClearSaved.setOnClickListener {
                viewModel.clearSaved()
                Snackbar.make(
                    root,
                    getString(R.string.snack_unsaved),
                    BaseTransientBottomBar.LENGTH_SHORT
                ).show()
            }
            buttonLogout.setOnClickListener {
                showLogoutConfirmationDialog()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.getProfile()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state -> updateUi(state) }
            }
        }
    }

    private fun updateUi(state: LoadState) {
        with(binding) {
            containerView.isVisible = state is LoadState.Content
            common.progressBar.isVisible = state is LoadState.Loading
            common.error.isVisible = state is LoadState.Error
            if (state is LoadState.Content) {
                val data = state.data as Profile
                loadProfileTexts(data)
                data.urlAvatar?.let { loadAvatar(it) }
            }
        }
    }

    private fun loadProfileTexts(data: Profile) {
        with(binding) {
            userName.text = data.name
            userId.text = getString(R.string.user_id, data.more_infos?.title ?: "N/A")
            karma.text = getString(R.string.karma, data.total_karma ?: 0)
            followers.text = getString(R.string.followers, data.more_infos?.subscribers ?: "0")
        }
    }

    private fun loadAvatar(url: String) {
        binding.imageView.loadImage(viewModel.getClearedUrlAvatar(url))
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.logout_title)
            .setMessage(R.string.logout_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.wipeToken()
                findNavController()
                    .navigate(R.id.action_navigation_profile_to_navigation_auth)
            }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }
}
