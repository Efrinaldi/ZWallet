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
import com.efrinaldi.zwallet.databinding.FragmentChangePasswordBinding
import com.efrinaldi.zwallet.databinding.FragmentChangePinBinding
import com.efrinaldi.zwallet.utils.PREFS_NAME

class ChangePinFragment : Fragment() {
    private lateinit var binding: FragmentChangePinBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.buttonBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_changePinFragment_to_profileFragment)
        }
    }

}