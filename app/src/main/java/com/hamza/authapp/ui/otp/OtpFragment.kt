package com.hamza.authapp.ui.otp;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.hamza.authapp.R
import com.hamza.authapp.databinding.OtpFragmentBinding
import com.hamza.authapp.utils.BaseFragment
import com.hamza.authapp.utils.ProgressLoading
import com.hamza.authapp.utils.Resources
import com.hamza.itiproject.utils.showToast
import com.hamza.itiproject.utils.visibilityGone
import com.hamza.itiproject.utils.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class OtpFragment : BaseFragment() {

    private var _binding: OtpFragmentBinding? = null
    private val binding get() = _binding!!
    var mobileNumber = "0"
    var verificationId = "0"

    private val viewModel: AuthPhoneViewModel by viewModels()

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
        showToast("Your mobile number is $mobileNumber")
        actions()
        observer()

    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isVerificationInProgress.collect() {
                    when (it) {
                        is Resources.Loading -> {
                            ProgressLoading.show(requireActivity())
                        }

                        is Resources.Success -> {
                            ProgressLoading.dismiss()
                            showToast(getString(R.string.verification_success) )
                            navigate(OtpFragmentDirections.actionOtpFragmentToLogoutFragment())
                        }

                        is Resources.Failed -> {
                            ProgressLoading.dismiss()
                            showToast(it.message.toString())
                        }

                        else -> Unit
                    }
                }
            }
        }
    }


    private fun actions() {
        binding.apply {
            btnConfirm.setOnClickListener {

                check()
            }
            txtResendotp.setOnClickListener {
                resentOtp()
            }
        }
    }

    private fun resentOtp() {

    }


    private fun check() {
        val otpCode = binding.edtOtpCode.text.trim().toString()
        if (otpCode.isEmpty()) {
            binding.edtOtpCode.error = getString(R.string.requried)
        } else {
            viewModel.signInWithVerificationCode(verificationId, otpCode)

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}