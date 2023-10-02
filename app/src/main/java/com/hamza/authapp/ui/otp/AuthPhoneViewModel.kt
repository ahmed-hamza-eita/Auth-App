package com.hamza.authapp.ui.otp

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hamza.authapp.utils.OTPFieldsState
import com.hamza.authapp.utils.Resources
import com.hamza.authapp.utils.VerificationOTPValidation
import com.hamza.authapp.utils.validOTP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AuthPhoneViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {

    private val _verificationId = MutableStateFlow<String?>(null)
    val verificationId get() = _verificationId
    private val _isVerificationInProgress =
        MutableStateFlow<Resources<Boolean>>(Resources.Unspecified())
    val isVerificationInProgress get() = _isVerificationInProgress

    private val _validation = Channel<OTPFieldsState>()
    val validation get() = _validation.receiveAsFlow()
    fun sendVerificationCode(phone: String, activity: Activity) {
        runBlocking {
            _isVerificationInProgress.emit(Resources.Loading())
        }
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {

                _isVerificationInProgress.value = Resources.Failed(e.message.toString())
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                _verificationId.value = verificationId
                _isVerificationInProgress.value =
                    Resources.Success(true, "onCodeSent: $verificationId")


                // Save verification ID and resending token so we can use them later
                // storedVerificationId = verificationId
                // resendToken = token
            }


        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+2$phone") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithVerificationCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential, code)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, code: String) {

        if (checkValidation(code)) {
            runBlocking {
                _isVerificationInProgress.emit(Resources.Loading())
            }
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isVerificationInProgress.value =
                        Resources.Success(false, "signInWithCredential success: ${task.result}")

                } else {
                    _isVerificationInProgress.value =
                        Resources.Failed("signInWithCredential failure: ${task.exception?.message}")

                }

            }
        } else {
            val otpFieldsState =
                OTPFieldsState(validOTP(code))
            viewModelScope.launch {
                _validation.send(otpFieldsState)
            }
        }
    }

    private fun checkValidation(
        codeSendVerification: String
    ): Boolean {
        val otpValidation = validOTP(codeSendVerification)
        return (otpValidation is VerificationOTPValidation.Success)
    }
}