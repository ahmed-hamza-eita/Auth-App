package com.hamza.authapp.ui.login;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hamza.authapp.R
import com.hamza.authapp.databinding.ForgetPasswordFragmentBinding
import com.hamza.authapp.ui.AuthViewModel
import com.hamza.authapp.ui.signup.SignupFragmentDirections
import com.hamza.authapp.utils.BaseFragment
import com.hamza.authapp.utils.Const
import com.hamza.authapp.utils.MySharedPreferences
import com.hamza.authapp.utils.NetworkState
import com.hamza.authapp.utils.ProgressLoading
import com.hamza.itiproject.utils.showToast
import com.sanctionco.jmail.JMail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ForgetPasswordFragment : BaseFragment() {

    private var _binding: ForgetPasswordFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.forget_password_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ForgetPasswordFragmentBinding.bind(view)

        actions()
        observer()
    }

    private fun observer() {

        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect { result ->
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
                        showToast(result)
                        navigate(ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToLoginFragment())
                    }


                    else -> {

                    }
                }
            }
        }
    }

    private fun actions() {
        binding.btnConfirm.setOnClickListener {
            check()
        }
    }

    private fun check() {
        val emial = binding.edtEmail.text?.trim().toString()
        if (emial.isEmpty()) {
            binding.edtEmail.error = getString(R.string.requried)
        } else if (JMail.isInvalid(emial)) {
            binding.edtEmail.error = getString(R.string.invalid)
        } else {
            viewModel.resetPasswordFun(emial)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}