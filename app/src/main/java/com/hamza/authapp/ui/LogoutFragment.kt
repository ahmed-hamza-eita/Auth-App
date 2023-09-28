package com.hamza.authapp.ui;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hamza.authapp.R
import com.hamza.authapp.databinding.LogoutFragmentBinding
import com.hamza.authapp.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LogoutFragment : BaseFragment() {

    private var _binding: LogoutFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
_binding = WallpaperFragmentBinding.inflate(layoutInflater, container, false)
        return _binding?.root
*/
        return inflater.inflate(R.layout.logout_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = LogoutFragmentBinding.bind(view)

        binding.checkedDone.playAnimation()


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}