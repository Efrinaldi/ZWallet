package com.efrinaldi.zwallet.ui.layout.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.databinding.FragmentManagePhoneBinding
import com.efrinaldi.zwallet.model.request.ManagePhoneRequest
import com.efrinaldi.zwallet.ui.layout.main.profile.ProfileViewModel
import com.efrinaldi.zwallet.ui.widget.LoadingDialog
import com.efrinaldi.zwallet.utils.PREFS_NAME
import com.efrinaldi.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class ManagePhoneFragment : Fragment() {
    private lateinit var binding: FragmentManagePhoneBinding
    private lateinit var prefs : SharedPreferences
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManagePhoneBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode((WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN))

        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }


        binding.btnSubmit.setOnClickListener {

            val request = ManagePhoneRequest(
                binding.phoneCode.text.toString()+
                        binding.phoneNumber.text.toString(),
            )

            viewModel.managePhone(request).observe(viewLifecycleOwner){
                when(it.state){
                    State.LOADING -> {
                        loadingDialog.start("Processing Your Request")
                    }
                    State.SUCCESS -> {
                        if (it.data?.status == HttpsURLConnection.HTTP_OK) {
                            loadingDialog.stop()
                            Toast.makeText(context, "Phone number changed", Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).popBackStack()
                        } else {
                            Toast.makeText(context, it.data?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    State.ERROR -> {
                        loadingDialog.stop()
                        Toast.makeText(context, it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }

    }
}