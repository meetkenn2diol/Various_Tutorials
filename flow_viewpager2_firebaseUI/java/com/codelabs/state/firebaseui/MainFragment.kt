/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelabs.state.firebaseui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codelabs.state.R
import com.codelabs.state.advancedcoroutine.ui.MainPlantActivity
import com.codelabs.state.databinding.FragmentMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class MainFragment : Fragment() {

    companion object {
        const val TAG = "MainFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    //Create an ActivityResultLauncher which registers a callback for the FirebaseUI Activity result contract:
    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            // Listen to the result of the sign in process.
            this.onSignInResult(result)
        }

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        // TODO Remove the two lines below once observeAuthenticationState is implemented.
        binding.welcomeText.text = viewModel.getFactToDisplay(requireContext())
        binding.authButton.text = getString(R.string.login_btn)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()

        //info when  observeAuthenticationState() is implemented, this block of code is no longer useful
        binding.authButton.setOnClickListener {
            // TODO call launchSignInFlow when authButton is clicked
            launchSignInFlow()
        }

        binding.settingsBtn.setOnClickListener {
            val action =
                com.codelabs.state.firebaseui.MainFragmentDirections.actionMainFragmentToSettingsFragment()
            findNavController().navigate(action)
        }
    }

    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */
    private fun observeAuthenticationState() {
        val factToDisplay = viewModel.getFactToDisplay(requireContext())

        // TODO Use the authenticationState variable from LoginViewModel to update the UI
        //  accordingly.
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    binding.authButton.text = getString(R.string.logout_button_text)
                    binding.authButton.setOnClickListener {
                        // TODO implement logging out user in next step
                        AuthUI.getInstance().signOut(requireContext())
                    }

                    // TODO 2. If the user is logged in,
                    // you can customize the welcome message they see by
                    // utilizing the getFactWithPersonalization() function provided
                    binding.welcomeText.text = getFactWithPersonalization(factToDisplay)
                }
                else -> {
                    // TODO 3. Lastly, if there is no logged-in user,
                    // auth_button should display Login and
                    //  launch the sign in screen when clicked.
                    binding.authButton.text = getString(R.string.login_button_text)
                    binding.authButton.setOnClickListener { launchSignInFlow() }
                    binding.welcomeText.text = factToDisplay
                }
            }
        })
        //  TODO If there is a logged-in user, authButton should display Logout. If the
        //   user is logged in, you can customize the welcome message by utilizing
        //   getFactWithPersonalition(). I

        // TODO If there is no logged in user, authButton should display Login and launch the sign
        //  in screen when clicked. There should also be no personalization of the message
        //  displayed.
    }


    private fun getFactWithPersonalization(fact: String): String {
        return String.format(
            resources.getString(
                R.string.welcome_message_authed,
                FirebaseAuth.getInstance().currentUser?.displayName,
                Character.toLowerCase(fact[0]) + fact.substring(1)
            )
        )
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        // Start by having log statements to know whether the user has signed in successfully
        if (result.resultCode == Activity.RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Timber.i("Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            Timber.i("Sign in unsuccessful ${response?.error?.errorCode} \n $response")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.goto_firebaseui_demo).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.goto_viewpager2_demo -> {
                val action =
                    com.codelabs.state.firebaseui.MainFragmentDirections.actionMainFragmentToViewPager2Fragment()
                findNavController().navigate(action)
                true
            }
            R.id.goto_advanced_coroutine_tutorial->{
                val intent = Intent(requireActivity(),MainPlantActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}