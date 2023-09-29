package com.hamza.authapp.ui.login;

import LanguageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hamza.authapp.R
import com.hamza.authapp.databinding.LoginFragmentBinding
import com.hamza.authapp.ui.AuthViewModel
import com.hamza.authapp.utils.BaseFragment
import com.hamza.authapp.utils.Const
import com.hamza.authapp.utils.MySharedPreferences
import com.hamza.authapp.utils.NetworkState
import com.hamza.authapp.utils.ProgressLoading
import com.hamza.itiproject.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

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
                        MySharedPreferences.putBoolean(Const.KEY_IS_SIGNED_IN,true)
                        navigate(LoginFragmentDirections.actionLoginFragmentToLogoutFragment())
                    }


                    else -> {
                        showToast("Done")
                    }
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