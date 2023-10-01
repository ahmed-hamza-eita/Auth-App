package com.hamza.authapp.ui.signup;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
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
import com.hamza.itiproject.utils.visibilityGone
import com.hamza.itiproject.utils.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import javax.inject.Inject

@AndroidEntryPoint
class AuthWithPhoneFragment : BaseFragment() {

    private var _binding: AuthWithPhoneFragmentBinding? = null
    private val binding get() = _binding!!


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
        }


          val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                ProgressLoading.dismiss()
                binding.btnConfirm.visibilityVisible()
            }

            override fun onVerificationFailed(e: FirebaseException) {

                e.printStackTrace()
                ProgressLoading.dismiss()
                binding.btnConfirm.visibilityVisible()
                showToast(e.message)
            }

            override fun onCodeSent(
                verificationId: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                ProgressLoading.dismiss()
                binding.btnConfirm.visibilityVisible()
                navigate(
                    AuthWithPhoneFragmentDirections.actionAuthWithPhoneFragmentToOtpFragment(
                        mobileNumber,
                        verificationId
                    )
                )
            }
        }


        ProgressLoading.show(requireActivity())
        binding.btnConfirm.visibilityGone()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+2$mobileNumber", 60L, TimeUnit.SECONDS,
            requireActivity(), callbacks
        )
    }





    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}