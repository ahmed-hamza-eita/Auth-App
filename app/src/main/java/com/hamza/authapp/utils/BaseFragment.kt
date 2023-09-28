package com.hamza.authapp.utils;

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController


open class BaseFragment : Fragment() {

    var myContext: Context? = null
    var myView: View? = null
    var myActivity: FragmentActivity? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        myActivity = requireActivity()
    }

    protected fun navigate(navDirections: NavDirections) {
        Navigation.findNavController(myView!!).navigate(navDirections!!)
    }

    protected fun navigate(navDirections: Int, bundle: Bundle) {
        findNavController().navigate(navDirections, bundle)
    }


}
