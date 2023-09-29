package com.hamza.authapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.hamza.authapp.repo.AuthRepo
import com.hamza.authapp.utils.Const
import com.hamza.authapp.utils.Const.Companion.KEY_IS_SIGNED_IN
import com.hamza.authapp.utils.MySharedPreferences
import com.hamza.authapp.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AuthRepo) : ViewModel() {


    private val _loginFlow = MutableStateFlow<NetworkState<FirebaseUser>?>(null)
    val loginFlow get() = _loginFlow


    private val _signupWithEmailFlow = MutableStateFlow<NetworkState<FirebaseUser>?>(null)
    val signupWithEmailFlow get() = _signupWithEmailFlow


    private val _signupWithGmailFlow = MutableStateFlow<NetworkState<FirebaseUser>?>(null)
    val signupWithGmailFlow get() = _signupWithGmailFlow


    private val _signupWithPhoneFlow = MutableStateFlow<NetworkState<FirebaseUser>?>(null)
    val signupWithPhoneFlow get() = _signupWithPhoneFlow


    private val _resetPassword = MutableStateFlow<NetworkState<FirebaseUser>?>(null)
    val resetPassword get() = _resetPassword

    val currentUser: FirebaseUser? get() = repo.currentUser

    init {
        if (repo.currentUser != null) {
            MySharedPreferences.putBoolean(KEY_IS_SIGNED_IN, true)
        }
    }

    fun login ( email: String, password: String) =
        viewModelScope.launch {
            _loginFlow.value = NetworkState.Loading
            val result = repo.loginWithEmailAndPassword(email, password)
            _loginFlow.value = result
        }

    fun signUpWithEmailAndPassword(name: String, email: String, password: String) =
        viewModelScope.launch {
            _signupWithEmailFlow.value = NetworkState.Loading
            val result = repo.signupWithEmailAndPassword(name, email, password)
            _signupWithEmailFlow.value = result
        }


    fun signUpWithGoogle(email: GoogleSignInAccount) =
        viewModelScope.launch {
            _signupWithGmailFlow.value = NetworkState.Loading
            val result = repo.signupWithGoogleAccount(email)
            _signupWithGmailFlow.value = result
        }

    fun signUpWithPhone(phone: PhoneAuthCredential) =
        viewModelScope.launch {
            _signupWithPhoneFlow.value = NetworkState.Loading
            val result = repo.signupWithPhoneNumber(phone)
            _signupWithPhoneFlow.value = result
        }

    fun resetPassword(email: String) = viewModelScope.launch {
        NetworkState.Loading
        val result = repo.resetPassword(email)
        _resetPassword.value = result
    }

    fun logout() {
        repo.logout()
        _loginFlow.value = null
        _signupWithEmailFlow.value = null
        _signupWithGmailFlow.value = null
        _signupWithPhoneFlow.value = null
    }
}