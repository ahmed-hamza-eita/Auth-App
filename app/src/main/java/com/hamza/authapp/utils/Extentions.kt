package com.hamza.itiproject.utils;

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import com.hamza.authapp.R

import io.github.muddz.styleabletoast.StyleableToast

fun Activity.showToast(message: Any?) {
    StyleableToast.makeText(this, "$message", R.style.toastStyle).show()
}

fun Fragment.showToast(message: Any?) {
    StyleableToast.makeText(requireActivity(), "$message", R.style.toastStyle).show()
}
fun View.visibilityGone() {
    this.visibility = View.GONE
}

fun View.visibilityVisible() {
    this.visibility = View.VISIBLE
}

