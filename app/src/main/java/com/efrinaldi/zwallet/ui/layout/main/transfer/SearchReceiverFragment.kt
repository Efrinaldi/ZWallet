package com.efrinaldi.zwallet.ui.layout.main.transfer

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.databinding.FragmentSearchReceiverBinding
import com.efrinaldi.zwallet.model.Contact
import com.efrinaldi.zwallet.ui.adapter.ContactAdapter
import com.efrinaldi.zwallet.ui.widget.LoadingDialog
import com.efrinaldi.zwallet.utils.PREFS_NAME
import com.efrinaldi.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class SearchReceiverFragment : Fragment() {
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var binding: FragmentSearchReceiverBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: TransferViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchReceiverBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener{
            Navigation.findNavController(view).popBackStack()
        }
        this.contactAdapter = ContactAdapter(listOf())
        { contact, _ ->
            viewModel.setSelectedContact(contact)
            Navigation.findNavController(view)
                .navigate(R.id.action_searchReceiverFragment_to_inputAmountFragment2)

            prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
            loadingDialog = LoadingDialog(requireActivity())

        }
        binding.recyclerTransactionContact.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactAdapter
        }
        viewModel.getContactUser().observe(viewLifecycleOwner) {
            when (it.state){
                State.LOADING -> {
                    binding.apply {
                        loadingIndicator.visibility = View.VISIBLE
                        recyclerTransactionContact.visibility = View.GONE
                    }
                }
                State.SUCCESS -> {
                    binding.apply {
                        loadingIndicator.visibility = View.GONE
                        recyclerTransactionContact.visibility = View.VISIBLE
                    }
                    if (it.data?.status == HttpsURLConnection.HTTP_OK) {
                        this.contactAdapter.apply {
                            addData(it.data.data!!)
                            notifyDataSetChanged()
                        }
                        binding.contactTotal.text = contactAdapter.itemCount.toString()
                    } else {
                        Toast.makeText(context, it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                State.ERROR -> {
                    binding.apply {
                        loadingIndicator.visibility = View.GONE
                        recyclerTransactionContact.visibility = View.VISIBLE
                    }
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }
    }

}
