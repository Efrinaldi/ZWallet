package com.efrinaldi.zwallet.ui.layout.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.databinding.FragmentChangePinBinding
import com.efrinaldi.zwallet.databinding.FragmentInputOtpBinding
import com.efrinaldi.zwallet.utils.PREFS_NAME

class InputOtpFragment : Fragment() {
    private lateinit var binding: FragmentInputOtpBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.btnConfirm.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_inputOtpFragment_to_loginFragment)
        }
    }

}