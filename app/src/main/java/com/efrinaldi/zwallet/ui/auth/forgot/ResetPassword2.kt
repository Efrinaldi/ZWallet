package com.efrinaldi.zwallet.ui.auth.forgot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.Navigation
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.databinding.FragmentResetPassword2Binding
import com.efrinaldi.zwallet.databinding.FragmentResetPasswordBinding

class ResetPassword2 : Fragment() {
    private lateinit var binding: FragmentResetPassword2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPassword2Binding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        binding.btnConfirm.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_resetPassword2_to_loginFragment)
        }
    }

}