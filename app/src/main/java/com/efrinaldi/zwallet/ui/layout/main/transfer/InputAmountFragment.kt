package com.efrinaldi.zwallet.ui.layout.main.transfer

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
import com.efrinaldi.zwallet.databinding.FragmentInputAmountBinding
import com.efrinaldi.zwallet.model.request.TransferRequest
import com.efrinaldi.zwallet.utils.BASE_URL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputAmountFragment : Fragment() {
    private lateinit var binding: FragmentInputAmountBinding
    private val viewModel: TransferViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputAmountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var getId: String?=null

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonContinueConfirmation.setOnClickListener {
            viewModel.setTransferParameter(TransferRequest(
                getId!!,
                binding.amountTransfer.text.toString().toInt(),
                binding.noteTransfer.text.toString()
            ))

            Navigation.findNavController(view).navigate(R.id.action_inputAmountFragment2_to_transferConfirmationFragment2)
        }
        viewModel.getBalance().observe(viewLifecycleOwner) {
            binding.apply {
                binding.textAmountAvailable.text = it.data?.data?.get(0)?.balance.toString()
            }
        }

        viewModel.getSelectedContact().observe(viewLifecycleOwner) {
            getId = it?.id.toString()
            binding.apply {
                nameContact.text = "${it?.name}"
                phoneContact.text = "${it?.phone}"
                Glide.with(imageContact).load(BASE_URL + it?.image)
                    .apply(RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.ic_baseline_person_24))
                    .into(imageContact)
            }
        }
    }
}