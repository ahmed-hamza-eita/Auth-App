package com.hamza.authapp.ui.signup;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
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
import com.hamza.itiproject.utils.visibilityGone
import com.hamza.itiproject.utils.visibilityVisible
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
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {

                e.printStackTrace()

                showToast(e.message)
            }

            override fun onCodeSent(
                newVerificationId: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                verificationId = newVerificationId
                ProgressLoading.dismiss()
                binding.btnConfirm.visibilityVisible()
                showToast(getString(R.string.otp_sent))
            }
        }


        ProgressLoading.show(requireActivity())
        binding.btnConfirm.visibilityGone()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+2$mobileNumber", 60L, TimeUnit.SECONDS,
            requireActivity(), callbacks
        )
    }


    private fun check() {
        val otpCode = binding.edtOtpCode.text.trim().toString()
        if (otpCode.isEmpty()) {
            binding.edtOtpCode.error = getString(R.string.requried)
        } else {
            if (verificationId != null) {
                ProgressLoading.show(requireActivity())
                binding.btnConfirm.visibilityGone()
                val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        ProgressLoading.dismiss()
                        binding.btnConfirm.visibilityVisible()
                        if (task.isSuccessful) {
                            navigate(OtpFragmentDirections.actionOtpFragmentToLogoutFragment())
                        } else {

                            showToast(getString(R.string.invalid_otp))
                        }
                    }


            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}