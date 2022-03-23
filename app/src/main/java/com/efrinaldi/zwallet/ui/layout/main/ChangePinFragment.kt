package com.efrinaldi.zwallet.ui.layout.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.databinding.FragmentChangePasswordBinding
import com.efrinaldi.zwallet.databinding.FragmentChangePinBinding
import com.efrinaldi.zwallet.ui.layout.main.profile.ProfileViewModel
import com.efrinaldi.zwallet.ui.widget.LoadingDialog
import com.efrinaldi.zwallet.utils.PREFS_NAME
import com.efrinaldi.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class ChangePinFragment : Fragment() {
    private lateinit var binding: FragmentChangePinBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel : ProfileViewModel by activityViewModels()
    var pin = mutableListOf<EditText>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePinBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextPin()
        binding.buttonBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_changePinFragment_to_profileFragment)
        }

        binding.buttonContinueToNewPin.setOnClickListener {
            val textPin = binding.pin1.text.toString() +
                    binding.pin2.text.toString() +
                    binding.pin3.text.toString() +
                    binding.pin4.text.toString() +
                    binding.pin5.text.toString() +
                    binding.pin6.text.toString()

            viewModel.checkPin(textPin.toInt()).observe(viewLifecycleOwner) {
                when (it.state) {
                    State.LOADING -> {
                        loadingDialog.start("Process Check PIN")
                    }
                    State.SUCCESS -> {
                        if (it.data?.status == HttpsURLConnection.HTTP_OK) {
                            loadingDialog.stop()
                            Navigation.findNavController(view).navigate(R.id.action_changePinFragment_to_newPinFragment2)
                        } else {
                            Toast.makeText(context, it.data?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    State.ERROR -> {
                        loadingDialog.start("PIN Isn't Correct")
                        Toast.makeText(context, it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun editTextPin() {
        pin.add(0, binding.pin1)
        pin.add(1, binding.pin2)
        pin.add(2, binding.pin3)
        pin.add(3, binding.pin4)
        pin.add(4, binding.pin5)
        pin.add(5, binding.pin6)
        pinHandler()
    }
    fun pinHandler() {
        for(i in 0..5) {
            pin.get(i).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    pin.get(i).setBackgroundResource(R.drawable.edit_text_after)
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (i ==5 && !pin.get(i).getText().toString().isEmpty()) {
                        pin.get(i).clearFocus()
                    } else if (!pin.get(i).getText().toString().isEmpty()) {
                        pin.get(i + 1).requestFocus()
                    }
                }
            })

            pin.get(i).setOnKeyListener(View.OnKeyListener { view, x, keyEvent ->
                if (keyEvent.action !== KeyEvent.ACTION_DOWN) {
                    return@OnKeyListener false
                }
                if (x == KeyEvent.KEYCODE_DEL && pin.get(i).getText().toString().isEmpty() && i != 0) {
                    pin.get(i - 1).setText("")
                    pin.get(i - 1).requestFocus()
                    pin.get(i - 1).setBackgroundResource(R.drawable.edit_text)
                }
                false
            })
        }
    }

}