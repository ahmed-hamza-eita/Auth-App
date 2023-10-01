package com.hamza.authapp.ui.login;

import LanguageManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.hamza.authapp.R
import com.hamza.authapp.databinding.LoginFragmentBinding
import com.hamza.authapp.ui.AuthViewModel
import com.hamza.authapp.utils.BaseFragment
import com.hamza.authapp.utils.Const
import com.hamza.authapp.utils.Const.Companion.GOOGLE_ACCOUNT_REQUEST
import com.hamza.authapp.utils.MySharedPreferences
import com.hamza.authapp.utils.NetworkState
import com.hamza.authapp.utils.ProgressLoading
import com.hamza.itiproject.utils.showToast
import com.sanctionco.jmail.JMail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var signInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.login_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = LoginFragmentBinding.bind(view)

        actions()
        observer()
    }

    private fun observer() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginFlow.collect { result ->
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
                        showToast(Const.SIGNUP_SUCCESS)
                        MySharedPreferences.putBoolean(Const.KEY_IS_SIGNED_IN, true)
                        navigate(LoginFragmentDirections.actionLoginFragmentToLogoutFragment())
                    }


                    else -> {

                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.signupWithGmailFlow.collect {
                when (it) {
                    is NetworkState.Loading -> {
                        ProgressLoading.show(requireActivity())
                    }

                    is NetworkState.Success -> {
                        ProgressLoading.dismiss()
                        showToast(Const.SIGNUP_SUCCESS_WITH_GOOGLE)
                        navigate(LoginFragmentDirections.actionLoginFragmentToLogoutFragment())
                    }

                    is NetworkState.Failure -> {
                        ProgressLoading.dismiss()
                        showToast(it.exception.message)
                    }

                    else -> {}
                }
            }
        }

    }


    private fun actions() {
        binding.apply {
            btnChangeLanuage.setOnClickListener {
                changeLanguage()
            }
            txtForgetPassword.setOnClickListener {
                navigate(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment())
            }
            txtGotoSignup.setOnClickListener {
                navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
            }
            btnLogInWithPhone.setOnClickListener {
                navigate(LoginFragmentDirections.actionLoginFragmentToAuthWithPhoneFragment())
            }
            btnLogin.setOnClickListener {
                checkUser()
            }
            btnLogInWithGmail.setOnClickListener {
                signInClient.signInIntent.also {
                    startActivityForResult(
                        it,
                        GOOGLE_ACCOUNT_REQUEST
                    )
                }
            }
            btnLogin.setOnClickListener {
                navigate(LoginFragmentDirections.actionLoginFragmentToAuthWithPhoneFragment())
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_ACCOUNT_REQUEST) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                viewModel.signUpWithGoogle(account)
            } catch (e: ApiException) {
                showToast(e.message.toString())
            }
        } else {
            showToast("Request Failed")
        }
    }

    private fun checkUser() {
        binding.apply {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (email.isEmpty()) {
                edtEmail.error = getString(R.string.requried)
            } else if (password.isEmpty()) {
                edtPassword.error = getString(R.string.requried)
            } else if (JMail.isInvalid(email)) {
                edtEmail.error = getString(R.string.invalid)
            } else {
                viewModel.login(email, password)
            }
        }
    }


    private fun changeLanguage() {

        val currentLocale = resources.configuration.locale

        if (currentLocale.language == "ar") {
            LanguageManager.setLocale(requireActivity(), "en")
        } else {
            LanguageManager.setLocale(requireActivity(), "ar")
        }

        recreate(requireActivity())

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}