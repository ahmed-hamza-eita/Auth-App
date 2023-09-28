package com.hamza.authapp.ui.login;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hamza.authapp.R
import com.hamza.authapp.databinding.ForgetPasswordFragmentBinding
import com.hamza.authapp.utils.BaseFragment


class ForgetPasswordFragment : BaseFragment() {

    private var _binding: ForgetPasswordFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
_binding = WallpaperFragmentBinding.inflate(layoutInflater, container, false)
        return _binding?.root
*/
        return inflater.inflate(R.layout.forget_password_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ForgetPasswordFragmentBinding.bind(view)


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}