package com.hamza.authapp.ui.signup;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hamza.authapp.R
import com.hamza.authapp.databinding.OtpFragmentBinding
import com.hamza.authapp.utils.BaseFragment


class OtpFragment : BaseFragment() {

    private var _binding: OtpFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
_binding = WallpaperFragmentBinding.inflate(layoutInflater, container, false)
        return _binding?.root
*/
        return inflater.inflate(R.layout.otp_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = OtpFragmentBinding.bind(view)


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}