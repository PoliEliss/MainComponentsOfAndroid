package com.rorono.maincomponentsofandroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rorono.maincomponentsofandroid.databinding.CardContactBinding

class PhoneNumbersAdapter :ListAdapter<Contact,PhoneNumbersAdapter.ContactViewHolder>(ItemCallback()) {

    inner class ContactViewHolder(binding: CardContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val textContactNumber = binding.textViewContactNumber
        private val textContactName = binding.textViewNameContact

        fun bind(contact: Contact) {
            textContactNumber.text = contact.tel
            textContactName.text = contact.name
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {

        val binding = CardContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}