package com.efrinaldi.zwallet.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.efrinaldi.zwallet.model.Contact
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.efrinaldi.zwallet.R
import com.efrinaldi.zwallet.databinding.ItemContactTransactionBinding
import com.efrinaldi.zwallet.utils.BASE_URL
import com.google.android.material.imageview.ShapeableImageView

class ContactAdapter (private var data: List<Contact>, private val clickListener: (Contact, View) -> Unit,): RecyclerView.Adapter<ContactAdapter.ContactAdapterHolder>() {

    private lateinit var contextAdapter: Context

    class ContactAdapterHolder(private val binding: ItemContactTransactionBinding): RecyclerView
    .ViewHolder(binding.root) {
        fun bindData(data: Contact, onClick: (Contact, View) -> Unit){
            binding.transactionName.text = data.name
            binding.transactionPhone.text = data.phone
            Glide.with(binding.imagecontacttransaction)
                .load(BASE_URL + data.image)
                .apply(
                    RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.ic_baseline_person_24)
                )
                .into(binding.imagecontacttransaction)

            binding.root.setOnClickListener { onClick(data, binding.root) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        this.contextAdapter = parent.context

        val binding = ItemContactTransactionBinding.inflate(inflater, parent, false)
        return ContactAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactAdapterHolder, position: Int) {
        holder.bindData(this.data[position],clickListener)
    }

    override fun getItemCount(): Int {
        return  this.data.size
    }

    fun addData(data: List<Contact>){
        this.data = data
    }
}