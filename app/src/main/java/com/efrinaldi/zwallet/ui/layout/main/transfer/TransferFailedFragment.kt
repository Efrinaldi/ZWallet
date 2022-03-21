package com.efrinaldi.zwallet.ui.layout.main.transfer

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.databinding.FragmentTransferFailedBinding
import com.efrinaldi.zwallet.utils.BASE_URL
import com.efrinaldi.zwallet.utils.Helper.formatPrice
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TransferFailedFragment : Fragment() {
    private lateinit var binding: FragmentTransferFailedBinding
    private val viewModel: TransferViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransferFailedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareData()
        binding.btnTryAgain.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_transferFailedFragment3_to_homeFragment)
        }
    }

    fun prepareData() {
        viewModel.getSelectedContact().observe(viewLifecycleOwner) {
            binding.receiverInfo.apply {
                transactionName.text = "${it.name}"
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
            if(it.notes.isNullOrEmpty()) {
                binding.valueNotes.text = "-"
            } else {
                binding.valueNotes.text = it.notes
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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