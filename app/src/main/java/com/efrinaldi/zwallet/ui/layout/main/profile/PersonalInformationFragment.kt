package com.efrinaldi.zwallet.ui.layout.main.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.databinding.FragmentPersonalInformationBinding
import com.efrinaldi.zwallet.ui.widget.LoadingDialog
import com.efrinaldi.zwallet.utils.PREFS_NAME
import com.efrinaldi.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class PersonalInformationFragment : Fragment() {
    private lateinit var binding: FragmentPersonalInformationBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: PersonalInformationViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalInformationBinding.inflate(layoutInflater)
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.buttonBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_personalInformationFragment_to_profileFragment)
        }
        prepareData()
    }

    private fun prepareData() {

        viewModel.getProfileInfo().observe(viewLifecycleOwner){
            when (it.state){
                State.LOADING -> {
                    loadingDialog.start("Processing Your Request")
                }

                State.SUCCESS -> {
                    if(it.data?.status == HttpsURLConnection.HTTP_OK){
                        binding.apply {
                            firstName.text = it.data.data?.firstname
                            lastName.text = it.data.data?.lastname
                            verifiedEmail.text = it.data.data?.email
                            phoneNumber.text = it.data.data?.phone
                        }
                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    loadingDialog.dismiss()
                }
                State.ERROR -> {
                    loadingDialog.dismiss()
                    Toast.makeText(context, "Failed to load your data", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }

}