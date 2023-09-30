package com.hamza.authapp.ui.signup;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.hamza.authapp.R
import com.hamza.authapp.databinding.OtpFragmentBinding
import com.hamza.authapp.ui.AuthViewModel
import com.hamza.authapp.utils.BaseFragment
import com.hamza.itiproject.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class OtpFragment : BaseFragment() {

    private var _binding: OtpFragmentBinding? = null
    private val binding get() = _binding!!
    var mobileNumber = "0"
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
        showToast("Your mobile number is $mobileNumber")
        actions()
        // observer()
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
            //viewModel.verifySmsCode(otpCode)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}