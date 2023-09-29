package com.hamza.authapp.ui.signup;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hamza.authapp.R
import com.hamza.authapp.databinding.SignupFragmentBinding
import com.hamza.authapp.ui.login.LoginFragmentDirections
import com.hamza.authapp.utils.BaseFragment


class SignupFragment : BaseFragment() {

    private var _binding: SignupFragmentBinding? = null
    private val binding get() = _binding!!


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

    }

    private fun actions() {
        binding.txtGotoLogin.setOnClickListener {
            navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
        }
        binding.btnSignupWithPhone.setOnClickListener {
            navigate(LoginFragmentDirections.actionLoginFragmentToAuthWithPhoneFragment())
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}