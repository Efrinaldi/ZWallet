package com.efrinaldi.zwallet.ui.layout.main.transfer

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.databinding.FragmentTransferConfirmationBinding
import com.efrinaldi.zwallet.ui.widget.LoadingDialog
import com.efrinaldi.zwallet.utils.BASE_URL
import com.efrinaldi.zwallet.utils.Helper.formatPrice
import com.efrinaldi.zwallet.utils.PREFS_NAME
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class TransferConfirmationFragment : Fragment() {
    private lateinit var binding: FragmentTransferConfirmationBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: TransferViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransferConfirmationBinding.inflate(layoutInflater)
        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnContinueToPin.setOnClickListener {
            loadingDialog.start("Processing...")
            AlertDialog.Builder(context)
                .setTitle("Transfer")
                .setMessage("Are you sure to proseccing this transfer?")
                .setPositiveButton("Yes") { _, _ ->
                    Navigation.findNavController(it).navigate(R.id.action_transferConfirmationFragment2_to_pinTransferConfirmationFragment2)
                    loadingDialog.stop()

                }.setNegativeButton("No") { _, _ ->
                    return@setNegativeButton
                    loadingDialog.stop()
                }.show()
        }


        viewModel.getSelectedContact().observe(viewLifecycleOwner) {
            binding.userInfo.apply {
                transactionName.text = "${it?.name}"
                transactionPhone.text = "${it?.phone}"
                Glide.with(imagecontacttransaction).load(BASE_URL + it?.image)
                    .apply(
                        RequestOptions.circleCropTransform()
                            .placeholder(R.drawable.ic_baseline_person_24))
                    .into(imagecontacttransaction)
            }
        }

        viewModel.getTransferParameter().observe(viewLifecycleOwner) {
            binding.valueAmount.formatPrice(it.amount.toString())
            if (it.notes.isNullOrEmpty()) {
                binding.valueNotes.text = "-"
            } else {
                binding.valueNotes.text = it.notes
            }
            // format date
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mma")
                val answer = current.format(formatter)
                binding.valueDate.text = answer
            } else {
                val date = Date()
                val formatter = SimpleDateFormat("MMM dd, yyyy - HH:mma")
                val answer = formatter.format(date)
                binding.valueDate.text = answer
            }
            viewModel.getBalance().observe(viewLifecycleOwner) {
                binding.valueBalance.text = it.data?.data?.get(0)?.balance.toString()
            }
        }
    }
}