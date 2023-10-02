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
import com.hamza.authapp.databinding.AuthWithPhoneFragmentBinding
import com.hamza.authapp.utils.BaseFragment
import com.hamza.authapp.utils.ProgressLoading
import com.hamza.authapp.utils.Resources
import com.hamza.itiproject.utils.showToast
import com.hamza.itiproject.utils.visibilityGone
import com.hamza.itiproject.utils.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class AuthWithPhoneFragment : BaseFragment() {

    private var _binding: AuthWithPhoneFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthPhoneViewModel by viewModels()
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
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isVerificationInProgress.collect {
                    when (it) {
                        is Resources.Loading -> {
                            ProgressLoading.show(requireActivity())
                        }

                        is Resources.Success -> {
                            ProgressLoading.dismiss()
                            val verificationId = viewModel.verificationId.value
                            if (verificationId != null) {
                                showToast(getString(R.string.otp_sent))
                                navigate(
                                    AuthWithPhoneFragmentDirections.actionAuthWithPhoneFragmentToOtpFragment(
                                        mobileNumber,
                                        verificationId
                                    )
                                )
                            } else {
                                showToast(getString(R.string.try_again))
                            }

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
        binding.btnConfirm.setOnClickListener {
            check()
        }

    }


    private fun check() {
        mobileNumber = binding.edtMobileNumber.text?.trim().toString()
        if (mobileNumber.isEmpty()) {
            binding.edtMobileNumber.error = getString(R.string.requried)
        } else {
            viewModel.sendVerificationCode(mobileNumber, requireActivity())
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}