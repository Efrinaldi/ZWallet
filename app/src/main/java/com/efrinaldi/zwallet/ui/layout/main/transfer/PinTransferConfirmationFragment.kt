package com.efrinaldi.zwallet.ui.layout.main.transfer

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
import com.efrinaldi.zwallet.databinding.FragmentPinTransferConfirmationBinding
import com.efrinaldi.zwallet.model.request.TransferRequest
import com.efrinaldi.zwallet.ui.widget.LoadingDialog
import com.efrinaldi.zwallet.utils.PREFS_NAME
import com.efrinaldi.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PinTransferConfirmationFragment : Fragment() {
    private lateinit var binding: FragmentPinTransferConfirmationBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: TransferViewModel by activityViewModels()
    var pin = mutableListOf<EditText>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinTransferConfirmationBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextPin()

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        binding.btnTransferNow.setOnClickListener {
            transfer(it)
        }
    }

    fun transfer(view: View) {
        var amount: String ?= null
        var notes: String ?= null
        var receiver: String ?= null
        var request: TransferRequest ?= null

        val numberPin = binding.pin1.text.toString() +
                binding.pin2.text.toString() +
                binding.pin3.text.toString() +
                binding.pin4.text.toString() +
                binding.pin5.text.toString() +
                binding.pin6.text.toString()

        viewModel.getSelectedContact().observe(viewLifecycleOwner) {
            var transferUserName = "${it?.name}"
            var transferUserPhone = "${it?.phone}"
        }

        viewModel.getTransferParameter().observe(viewLifecycleOwner) {
            request = it
            binding.apply {
                receiver = "${it.receiver}"
                amount = "${it.amount}"
                notes = "${it.notes}"
            }
        }

        viewModel.transfer(request!!, numberPin).observe(viewLifecycleOwner) {
            when(it.state) {
                State.LOADING -> {
                    loadingDialog.start("Transfer being process")
                }
                State.SUCCESS -> {
                    loadingDialog.start("Successully Transfered")
                    loadingDialog.stop()
                    Navigation.findNavController(view).navigate(R.id.action_pinTransferConfirmationFragment2_to_transferSuccessFragment3)
                }
                State.ERROR -> {
                    loadingDialog.stop()
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.action_pinTransferConfirmationFragment2_to_transferFailedFragment3)
                }
            }
        }
    }

    fun editTextPin() {
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

