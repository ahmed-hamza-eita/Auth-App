package com.hamza.authapp.ui.signup;

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.hamza.authapp.R
import com.hamza.authapp.databinding.SignupFragmentBinding
import com.hamza.authapp.ui.AuthViewModel
import com.hamza.authapp.ui.login.LoginFragmentDirections
import com.hamza.authapp.utils.BaseFragment
import com.hamza.authapp.utils.Const
import com.hamza.authapp.utils.Const.Companion.GOOGLE_ACCOUNT_REQUEST
import com.hamza.authapp.utils.Const.Companion.SIGNUP_SUCCESS
import com.hamza.authapp.utils.MySharedPreferences
import com.hamza.authapp.utils.NetworkState
import com.hamza.authapp.utils.ProgressLoading
import com.hamza.itiproject.utils.showToast
import com.sanctionco.jmail.JMail
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : BaseFragment() {

    private var _binding: SignupFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var signInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.signup_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SignupFragmentBinding.bind(view)
        actions()
        observer()

    }

    private fun observer() {
        lifecycleScope.launchWhenStarted {
            viewModel.signupWithEmailFlow.collect { result ->
                when (result) {
                    is NetworkState.Loading -> {
                        ProgressLoading.show(requireActivity())
                    }

                    is NetworkState.Failure -> {
                        showToast(result.exception.message)
                        ProgressLoading.dismiss()

                    }

                    is NetworkState.Success -> {
                        ProgressLoading.dismiss()
                        showToast(SIGNUP_SUCCESS)
                        MySharedPreferences.putBoolean(Const.KEY_IS_SIGNED_IN, true)
                        navigate(SignupFragmentDirections.actionSignupFragmentToLogoutFragment())
                    }


                    else -> {

                    }
                }
            }
        }

    }

    private fun actions() {
        binding.apply {
            txtGotoLogin.setOnClickListener {
                navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
            }

            btnSignup.setOnClickListener {
                checkUser()
            }

        }
    }
     private fun checkUser() {
        binding.apply {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val name = edtName.text.toString()
            if (email.isEmpty()) {
                edtEmail.error = getString(R.string.requried)
            } else if (password.isEmpty()) {
                edtPassword.error = getString(R.string.requried)
            } else if (name.isEmpty()) {
                edtName.error = getString(R.string.requried)
            } else if (JMail.isInvalid(email)) {
                edtEmail.error = getString(R.string.invalid)
            } else {
                viewModel.signUpWithEmailAndPassword(name, email, password)
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}