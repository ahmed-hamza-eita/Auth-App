package com.hamza.authapp.ui.signup;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hamza.authapp.R
import com.hamza.authapp.databinding.AuthWithPhoneFragmentBinding
import com.hamza.authapp.ui.AuthViewModel
import com.hamza.authapp.utils.BaseFragment
import com.hamza.authapp.utils.MySharedPreferences


class AuthWithPhoneFragment : BaseFragment() {

    private var _binding: AuthWithPhoneFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
_binding = WallpaperFragmentBinding.inflate(layoutInflater, container, false)
        return _binding?.root
*/
        return inflater.inflate(R.layout.auth_with_phone_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = AuthWithPhoneFragmentBinding.bind(view)

        actions()
    }

    private fun actions() {
        val mobileNumber = binding.edtMobileNumber.text?.trim().toString()
        if (mobileNumber.isEmpty()) {
            binding.edtMobileNumber.error = getString(R.string.requried)
        } else {
            MySharedPreferences.setUserPhone(mobileNumber)
            navigate(AuthWithPhoneFragmentDirections.actionAuthWithPhoneFragmentToOtpFragment())
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}