package com.hamza.authapp.utils

import java.util.regex.Pattern

class Const {
    companion object {
        const val KEY_IS_SIGNED_IN = "signed"
        const val CLIENT_ID = "client id"
        const val LOGIN_SUCCESS = "Login Success"
        const val RESET_PASSWORD = "Reset Password"
        const val SIGNUP_SUCCESS_WITH_GOOGLE = "SignUp  with google account success"
        const val SIGNUP_SUCCESS = "SignUp success"
        const val SIGNUP_SUCCESS_WITH_PHONE = "SignUp  with mobile number success"
        const val LOGOUT = "Signed out"
        val MOBILE_NO_PATTERN =Pattern.compile("^((\\+92)?(0092)?(92)?(0)?)(3)([0-9]{2})((-?)|( ?))([0-9]{7})")
        const val GOOGLE_ACCOUNT_REQUEST = 10
        const val DEFAULT_WEB_CLIENT_ID =
            "301211341822-if3ra09a1upimea834vq73qu4gjfl2nl.apps.googleusercontent.com"
    }
}