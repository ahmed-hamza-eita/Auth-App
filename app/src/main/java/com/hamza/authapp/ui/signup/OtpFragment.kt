package com.hamza.authapp.ui.signup;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hamza.authapp.R
import com.hamza.authapp.databinding.OtpFragmentBinding
import com.hamza.authapp.ui.AuthViewModel
import com.hamza.authapp.utils.BaseFragment
import com.hamza.authapp.utils.Const
import com.hamza.authapp.utils.MySharedPreferences
import com.hamza.authapp.utils.NetworkState
import com.hamza.authapp.utils.ProgressLoading
import com.hamza.itiproject.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class OtpFragment : BaseFragment() {

    private var _binding: OtpFragmentBinding? = null
    private val binding get() = _binding!!
    var mobileNumber = "0"
    var verificationId = "0"
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.otp_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = OtpFragmentBinding.bind(view)
        mobileNumber = OtpFragmentArgs.fromBundle(requireArguments()).mobileNumber
        verificationId = OtpFragmentArgs.fromBundle(requireArguments()).verificationId
        showToast("Your mobile number is $mobileNumber and verification $verificationId")
        actions()
        observer()
    }

    private fun observer() {
        lifecycleScope.launchWhenStarted {
            viewModel.verifySmsCode.collect {
                when (it) {
                    is NetworkState.Loading -> {
                        ProgressLoading.show(requireActivity())
                    }

                    is NetworkState.Success -> {
                        showToast(Const.SIGNUP_SUCCESS_WITH_PHONE)
                        MySharedPreferences.setUserPhone("0")
                        navigate(OtpFragmentDirections.actionOtpFragmentToLogoutFragment())
                        ProgressLoading.dismiss()
                    }

                    is NetworkState.Failure -> {
                        showToast(it.exception.message)
                        ProgressLoading.dismiss()

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
        val otpCode = binding.edtOtpCode.text.trim().toString()
        if (otpCode.isEmpty()) {
            binding.edtOtpCode.error = getString(R.string.requried)
        } else {

            viewModel.verifySmsCode(otpCode)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}