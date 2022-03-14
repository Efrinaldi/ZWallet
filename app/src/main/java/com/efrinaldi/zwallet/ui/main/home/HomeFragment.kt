package com.efrinaldi.zwallet.ui.main.home

import adapter.TransactionAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.data.Transaction
import com.efrinaldi.zwallet.databinding.FragmentHomeBinding
import com.efrinaldi.zwallet.model.APIResponse
import com.efrinaldi.zwallet.model.Balance
import com.efrinaldi.zwallet.network.NetworkConfig
import com.efrinaldi.zwallet.ui.viewModelsFactory
import com.efrinaldi.zwallet.utils.Helper.formatPrice
import com.efrinaldi.zwallet.utils.PREFS_NAME
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
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
                    balance.tvTranscationName.text = it.data.data?.get(0)?.name
                }
            }
            else {
                Toast.makeText(context, "Authentication failed: Wrong email/password", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.getInvoice().observe(viewLifecycleOwner){

            if (it.data?.status == HttpsURLConnection.HTTP_OK){
                this.transactionAdapter.apply {
                    addData(it.data?.data!!)
                    notifyDataSetChanged()
                }
            }
            else {
                Toast.makeText(context, "Authentication failed: Wrong email/password", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    
}