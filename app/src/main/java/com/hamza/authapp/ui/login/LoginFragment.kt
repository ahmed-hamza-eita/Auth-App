package com.hamza.authapp.ui.login;

import LanguageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import com.hamza.authapp.R
import com.hamza.authapp.databinding.LoginFragmentBinding
import com.hamza.authapp.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!


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
    }

    private fun actions() {
        binding.btnChangeLanuage.setOnClickListener {
            changeLanguage()
        }
        binding.txtForgetPassword.setOnClickListener {
            navigate(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment())
        }
        binding.txtGotoSignup.setOnClickListener {
            navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }
        binding.btnLogInWithPhone.setOnClickListener {
            navigate(LoginFragmentDirections.actionLoginFragmentToAuthWithPhoneFragment())
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