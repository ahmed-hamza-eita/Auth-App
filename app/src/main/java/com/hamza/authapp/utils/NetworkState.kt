package com.hamza.authapp.utils

//sealed class NetworkState {
//
//    object Loading : NetworkState()
//    data class Success(val message: String) : NetworkState()
//    data class Error(val message: String) : NetworkState()
//}


sealed class NetworkState<out R> {
    data class Success<out R>(val result: R) : NetworkState<R>()
    data class Failure(val exception: Exception) : NetworkState<Nothing>()
    object Loading : NetworkState<Nothing>()
    object VoidData : NetworkState<Nothing>()

}