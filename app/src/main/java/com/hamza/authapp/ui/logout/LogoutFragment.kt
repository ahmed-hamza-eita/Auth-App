package com.hamza.authapp.ui.logout;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hamza.authapp.R
import com.hamza.authapp.databinding.LogoutFragmentBinding
import com.hamza.authapp.ui.AuthViewModel
import com.hamza.authapp.ui.login.LoginFragmentDirections
import com.hamza.authapp.utils.BaseFragment
import com.hamza.authapp.utils.Const
import com.hamza.authapp.utils.Const.Companion.LOGOUT
import com.hamza.authapp.utils.MySharedPreferences
import com.hamza.itiproject.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LogoutFragment : BaseFragment() {

    private var _binding: LogoutFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.logout_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = LogoutFragmentBinding.bind(view)

        actions()
        showToast(viewModel.currentUser?.email)


    }

    private fun actions() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            showToast(LOGOUT)
            navigate(LogoutFragmentDirections.actionLogoutFragmentToLoginFragment())
            MySharedPreferences.clear()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}