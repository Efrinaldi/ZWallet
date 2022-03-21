package com.efrinaldi.zwallet.ui.layout.auth.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.databinding.FragmentLoginBinding
import com.efrinaldi.zwallet.ui.layout.main.MainActivity
import com.efrinaldi.zwallet.utils.*
import com.efrinaldi.zwallet.ui.widget.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var preferences : SharedPreferences
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        preferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)


        binding.inputPassword.addTextChangedListener {
            if (binding.inputPassword.text.length > 8) {
                binding.btnLogin.setBackgroundResource(R.drawable.background_button_auth_active)
                binding.btnLogin.setTextColor(Color.parseColor("#FFFFFF"))
            } else if (binding.inputPassword.text.length <= 8) {
                binding.btnLogin.setBackgroundResource(R.drawable.background_button_auth)
                binding.btnLogin.setTextColor(Color.parseColor("#9DA6B5"))
            }
        }

        binding.btnLogin.setOnClickListener {
            if(binding.inputEmail.text.isNullOrEmpty() || binding.inputPassword.text.isNullOrEmpty()){
                Toast.makeText(activity, "email/password is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val response = viewModel.login(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )

            response.observe(viewLifecycleOwner) {
                when (it.state) {
                    State.LOADING -> {
                        loadingDialog.start("Processing Your Request")

                    }
                    State.SUCCESS -> {
                        loadingDialog.stop()
                        if (it.data?.status == HttpsURLConnection.HTTP_OK) {
                            with (preferences.edit()) {
                                putBoolean(KEY_LOGGED_IN, true)
                                putString(KEY_USER_EMAIL, it.data.data?.email)
                                putString(KEY_USER_TOKEN, it.data.data?.token)
                                putString(KEY_USER_REFRESH_TOKEN, it.data.data?.refreshToken)
                                apply()
                            }
                            if(it.data.data?.hasPin == true) {
                                loadingDialog.stop()
                                Handler().postDelayed({
                                    val intent = Intent(activity, MainActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish()
                                }, 1000)
                            } else {
                                loadingDialog.stop()
                                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_createPinFragment)
                            }
                        } else {
                            Toast.makeText(context, it.data?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    State.ERROR -> {
                        loadingDialog.stop()
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }


        binding.gotosignup.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.loginActionRegister)
        }

        binding.textForgotPassword.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_resetPassword)
        }

    }

}