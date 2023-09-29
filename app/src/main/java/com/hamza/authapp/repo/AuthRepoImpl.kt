package com.hamza.authapp.repo

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.hamza.authapp.utils.Const
import com.hamza.authapp.utils.Const.Companion.RESET_PASSWORD
import com.hamza.authapp.utils.NetworkState
import com.hamza.authapp.utils.await
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(private val auth: FirebaseAuth) : AuthRepo {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): NetworkState<FirebaseUser> {
        return try {
            NetworkState.Loading
            val result = auth.signInWithEmailAndPassword(email, password).await()
            NetworkState.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkState.Failure(e)
        }

    }

    override suspend fun signupWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): NetworkState<FirebaseUser> {
        return try {
            NetworkState.Loading
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            NetworkState.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkState.Failure(e)
        }
    }

    override suspend fun resetPassword(email: String): NetworkState<FirebaseUser> {
        return try {
            NetworkState.Loading
            val result = auth.sendPasswordResetEmail(email).await()
            NetworkState.Loading
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkState.Failure(e)
        }
    }

    override suspend fun signupWithGoogleAccount(email: GoogleSignInAccount): NetworkState<FirebaseUser> {
        return try {
            NetworkState.Loading
            val credentials = GoogleAuthProvider.getCredential(email.idToken, null)
            val result = auth.signInWithCredential(credentials).await()
            NetworkState.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkState.Failure(e)
        }
    }

    override suspend fun signupWithPhoneNumber(phone: PhoneAuthCredential): NetworkState<FirebaseUser> {
        return try {
            NetworkState.Loading
            val result = auth.signInWithCredential(phone).await()
            NetworkState.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkState.Failure(e)
        }
    }

    override fun logout() {
        auth.signOut()
    }
}