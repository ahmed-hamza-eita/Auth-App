package com.hamza.authapp.ui;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hamza.authapp.R
import com.hamza.authapp.databinding.SplashScreenBinding
import com.hamza.authapp.utils.BaseFragment
import android.os.Handler
import android.os.Looper
import com.hamza.authapp.utils.Const.Companion.KEY_IS_SIGNED_IN
import com.hamza.authapp.utils.MySharedPreferences
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashScreen : BaseFragment() {

    private var _binding: SplashScreenBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
_binding = WallpaperFragmentBinding.inflate(layoutInflater, container, false)
        return _binding?.root
*/
        return inflater.inflate(R.layout.splash_screen, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SplashScreenBinding.bind(view)
        startSplash()
    }

    private fun startSplash() {
        binding.s1.playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({

            isUserSignedIn()
        }, 4000)
    }

    fun isUserSignedIn() {
        if (!MySharedPreferences.getBoolean(KEY_IS_SIGNED_IN)) {
            navigate(SplashScreenDirections.actionSplashScreenToLoginFragment())
        } else {
            navigate(SplashScreenDirections.actionSplashScreenToLogoutFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}