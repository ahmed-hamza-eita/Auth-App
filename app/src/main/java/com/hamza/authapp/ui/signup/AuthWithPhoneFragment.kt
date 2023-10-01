package com.hamza.authapp.ui.signup;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hamza.authapp.R
import com.hamza.authapp.databinding.AuthWithPhoneFragmentBinding
import com.hamza.authapp.ui.AuthViewModel
import com.hamza.authapp.ui.login.LoginFragmentDirections
import com.hamza.authapp.utils.BaseFragment
import com.hamza.authapp.utils.Const
import com.hamza.authapp.utils.MySharedPreferences
import com.hamza.authapp.utils.NetworkState
import com.hamza.authapp.utils.ProgressLoading
import com.hamza.itiproject.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import javax.inject.Inject

@AndroidEntryPoint
class AuthWithPhoneFragment : BaseFragment() {

    private var _binding: AuthWithPhoneFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()
    private var mobileNumber = ""

    @Inject
    lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.auth_with_phone_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = AuthWithPhoneFragmentBinding.bind(view)

        actions()
        observer()
    }

    private fun observer() {
        lifecycleScope.launchWhenStarted {
            viewModel.code.collect {
                when (it) {
                    is NetworkState.Loading -> {
                        ProgressLoading.show(requireActivity())
                    }

                    is NetworkState.Success -> {
                        ProgressLoading.dismiss()
                       // showToast(Const.SIGNUP_SUCCESS_WITH_PHONE)
                        navigate(
                            AuthWithPhoneFragmentDirections.actionAuthWithPhoneFragmentToOtpFragment(
                                mobileNumber, it.toString()
                            )
                        )
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
        binding.btnConfirm.setOnClickListener {
            check()
        }

    }

    private fun check() {
        mobileNumber = binding.edtMobileNumber.text?.trim().toString()
        if (mobileNumber.isEmpty()) {
            binding.edtMobileNumber.error = getString(R.string.requried)
        } else {
            sendVerifactionCode(mobileNumber)

        }
    }

    private fun sendVerifactionCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber("+2${phoneNumber}")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(viewModel.callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}