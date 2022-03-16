package com.efrinaldi.zwallet.ui.layout.main.home

import com.efrinaldi.zwallet.ui.adapter.TransactionAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.data.Transaction
import com.efrinaldi.zwallet.databinding.FragmentHomeBinding
import com.efrinaldi.zwallet.ui.viewModelsFactory
import com.efrinaldi.zwallet.utils.PREFS_NAME
import com.efrinaldi.zwallet.utils.State
import com.efrinaldi.zwallet.ui.widget.LoadingDialog
import com.efrinaldi.zwallet.utils.Helper.formatPrice
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        prepareData()

        binding.profileImage.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.buttonTopUp.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_topUpFragment)
        }

    }


    private fun prepareData() {
        this.transactionAdapter = TransactionAdapter(listOf())
        binding.recyclerTransaction.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }

        viewModel.getBalance().observe(viewLifecycleOwner) {
            when (it.state) {
                State.LOADING -> {
                    loadingDialog.start("Fetching Your Personal Data")
                }
                State.SUCCESS -> {
                    loadingDialog.stop()
                    if (it.data?.status == HttpsURLConnection.HTTP_OK) {
                        binding.apply {
                            currentBalance.formatPrice(it.data.data?.get(0)?.balance.toString())
                            userPhoneNumber.text = it.data.data?.get(0)?.phone
                            userName.text = it.data.data?.get(0)?.name
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Authentication failed: Wrong email/password",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                State.ERROR -> {
                    loadingDialog.stop()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getInvoice().observe(viewLifecycleOwner) {
            when (it.state) {
                State.LOADING -> {
                    binding.apply {
                        loadingIndicator.visibility = View.VISIBLE
                        recyclerTransaction.visibility = View.GONE
                    }
                }
                State.SUCCESS -> {
                    binding.apply {
                        loadingIndicator.visibility = View.GONE
                        recyclerTransaction.visibility = View.VISIBLE
                    }
                    if (it.data?.status == HttpsURLConnection.HTTP_OK) {
                        this.transactionAdapter.apply {
                            addData(it.data.data!!)
                            notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                State.ERROR -> {
                    binding.apply {
                        loadingIndicator.visibility = View.GONE
                        recyclerTransaction.visibility = View.VISIBLE
                    }
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }
    }
}
