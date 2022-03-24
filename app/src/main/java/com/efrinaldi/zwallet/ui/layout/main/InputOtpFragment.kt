package com.efrinaldi.zwallet.ui.layout.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.databinding.FragmentChangePinBinding
import com.efrinaldi.zwallet.databinding.FragmentInputOtpBinding
import com.efrinaldi.zwallet.ui.layout.auth.AuthActivity
import com.efrinaldi.zwallet.ui.widget.LoadingDialog
import com.efrinaldi.zwallet.utils.KEY_REGISTER_MAIL
import com.efrinaldi.zwallet.utils.PREFS_NAME
import com.efrinaldi.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputOtpFragment : Fragment() {
    private lateinit var binding: FragmentInputOtpBinding
    private lateinit var prefs: SharedPreferences
    var otp = mutableListOf<EditText>()
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: OtpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputOtpBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode((WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN))

        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        iniEditText()

        binding.btnConfirm.setOnClickListener {
            val otpNumber = binding.pin1.text.toString()+
                    binding.pin2.text.toString()+
                    binding.pin3.text.toString()+
                    binding.pin4.text.toString()+
                    binding.pin5.text.toString()+
                    binding.pin6.text.toString()

            val email = prefs.getString(KEY_REGISTER_MAIL,"")
            viewModel.otpActivation(email!!, otpNumber).observe(viewLifecycleOwner){
                when(it.state){
                    State.LOADING->{
                        loadingDialog.start("Processing the OTP")
                    }
                    State.SUCCESS->{
                        loadingDialog.stop()
                        Toast.makeText(context, "Account Activated", Toast.LENGTH_SHORT).show()

                        val intent = Intent(activity, AuthActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    State.ERROR->{
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                        loadingDialog.stop()
                        Navigation.findNavController(view).navigate(R.id.action_inputOtpFragment_self)

                    }
                }

            }
        }
    }

    fun iniEditText(){
        otp.add(0, binding.pin1)
        otp.add(1, binding.pin2)
        otp.add(2, binding.pin3)
        otp.add(3, binding.pin4)
        otp.add(4, binding.pin5)
        otp.add(5, binding.pin6)
        otpHandler()
    }

    fun otpHandler(){
        for(i in 0..5){
            otp.get(i).addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence,
                    start : Int,
                    count : Int,
                    after : Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    otp.get(i).setBackgroundResource(R.drawable.background_change_pin_after)
                }

                override fun afterTextChanged(s: Editable) {
                    if (i == 5 && !otp.get(i).getText().toString().isEmpty()){
                        otp.get(i).clearFocus()
                    }  else if (!otp.get(i).getText().toString().isEmpty()) {
                        otp.get(i + 1)
                            .requestFocus()
                    }
                }
            })
            otp.get(i).setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (event.action !== KeyEvent.ACTION_DOWN) {
                    return@OnKeyListener false //Dont get confused by this, it is because onKeyListener is called twice and this condition is to avoid it.
                }
                if (keyCode == KeyEvent.KEYCODE_DEL && otp.get(i).getText().toString()
                        .isEmpty() && i != 0
                ) {
                    otp.get(i - 1).setText("")
                    otp.get(i - 1).requestFocus()
                    otp.get(i).setBackgroundResource(R.drawable.background_change_pin)
                }
                false
            })
        }
    }
}