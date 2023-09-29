package com.hamza.authapp.repo

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.hamza.authapp.utils.NetworkState

interface AuthRepo {
    val currentUser: FirebaseUser?

    suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): NetworkState<FirebaseUser>

    suspend fun signupWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): NetworkState<FirebaseUser>
    suspend fun resetPassword(email: String): NetworkState<FirebaseUser>

    suspend fun signupWithGoogleAccount(email: GoogleSignInAccount):NetworkState<FirebaseUser>
    suspend fun signupWithPhoneNumber(phone: PhoneAuthCredential):NetworkState<FirebaseUser>

    fun logout()
}