package com.efrinaldi.zwallet.ui.layout.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.efrinaldi.zwallet.databinding.FragmentChangePasswordBinding
import com.efrinaldi.zwallet.model.APIResponse
import com.efrinaldi.zwallet.model.User
import com.efrinaldi.zwallet.model.request.ChangePasswordRequest
import com.efrinaldi.zwallet.network.NetworkConfig
import com.efrinaldi.zwallet.ui.widget.LoadingDialog
import com.efrinaldi.zwallet.utils.PREFS_NAME
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode((WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN))
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())

        binding.buttonBack.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        binding.btnContinueChangePassword.setOnClickListener {
            if( binding.currentPassword.text.isNullOrEmpty() || binding.newPassword1.text.isNullOrEmpty() || binding.newPassword2.text.isNullOrEmpty()){
                Toast.makeText(activity, "Current Password or New Password is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.newPassword1.text.isNullOrEmpty() != binding.newPassword2.text.isNullOrEmpty()){
                Toast.makeText(activity, "Password doesn't match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val changePasswordRequest = ChangePasswordRequest(
                binding.newPassword1.text.toString(),
                binding.currentPassword.text.toString(),
            )

            NetworkConfig(context).buildApi().changePassword(changePasswordRequest)
                .enqueue(object : Callback<APIResponse<User>>{
                    override fun onResponse(
                        call: Call<APIResponse<User>>,
                        response: Response<APIResponse<User>>
                    ) {
                        if (response.body()?.status != HttpsURLConnection.HTTP_OK){
                            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            val res = response.body()!!.message
                            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).popBackStack()
                        }
                    }
                    override fun onFailure(call: Call<APIResponse<User>>, t: Throwable) {
                        Toast.makeText(context, "Failed to change password", Toast.LENGTH_SHORT).show()
                    }
                })
        }

    }


}