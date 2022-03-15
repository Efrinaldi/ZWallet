package com.efrinaldi.zwallet.ui.main.home

import com.efrinaldi.zwallet.adapter.TransactionAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.data.Transaction
import com.efrinaldi.zwallet.databinding.FragmentHomeBinding
import com.efrinaldi.zwallet.ui.viewModelsFactory
import com.efrinaldi.zwallet.utils.Helper.formatPrice
import com.efrinaldi.zwallet.utils.PREFS_NAME
import com.efrinaldi.zwallet.utils.State
import javax.net.ssl.HttpsURLConnection

class HomeFragment : Fragment() {
    private val transactionData = mutableListOf<Transaction>()
    private lateinit var transactionAdapter: TransactionAdapter
    private  lateinit var binding: FragmentHomeBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: HomeViewModel by viewModelsFactory { HomeViewModel(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        prepareData()

        binding.balance.imagelisttransaction.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.balance.BtnTopUP.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_topUpFragment)
        }

    }



    private fun prepareData(){
        this.transactionAdapter = TransactionAdapter(listOf())
        binding.rvTransaction.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }
        viewModel.getBalance().observe(viewLifecycleOwner){

            if (it.data?.status == HttpsURLConnection.HTTP_OK){
                binding.apply {
                    balance.nominal.formatPrice(it.data.data?.get(0)?.balance.toString())
                    balance.telepon.text = it.data.data?.get(0)?.phone
                }
            }
            else {
                Toast.makeText(context, "Authentication failed: Wrong email/password", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.getInvoice().observe(viewLifecycleOwner){
            when (it.state) {
                State.LOADING -> {
                    binding.apply {
                        loadingIndicator.visibility = View.VISIBLE
                        rvTransaction.visibility = View.GONE
                    }
                }
                State.SUCCESS -> {
                    binding.apply {
                        loadingIndicator.visibility = View.VISIBLE
                        rvTransaction.visibility = View.GONE
                    }
                    if (it.data?.status == HttpsURLConnection.HTTP_OK) {
                        this.transactionAdapter.apply {
                            addData(it.data.data!!)
                            notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(context, it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                State.ERROR -> {
                    binding.apply {
                        loadingIndicator.visibility = View.GONE
                        rvTransaction.visibility
                    }
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                        .show()
                    }
                }
            }
        }
}