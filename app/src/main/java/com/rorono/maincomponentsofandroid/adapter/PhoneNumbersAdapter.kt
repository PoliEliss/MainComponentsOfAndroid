package com.rorono.maincomponentsofandroid.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rorono.maincomponentsofandroid.models.Contact
import com.rorono.maincomponentsofandroid.ItemCallback
import com.rorono.maincomponentsofandroid.databinding.ItemContactBinding


class PhoneNumbersAdapter : ListAdapter<Contact, PhoneNumbersAdapter.ContactViewHolder>(ItemCallback()) {
    lateinit var onItemClickListener: OnIemClickListener
    fun setOnListener(listener: OnIemClickListener) {
        onItemClickListener = listener
    }

    inner class ContactViewHolder(binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val textContactNumber = binding.textViewContactNumber
        private val textContactName = binding.textViewNameContact
        private val ivAvatar = binding.ivAvatarContacts
        private val ivCallContact = binding.ivCallContact

        fun bind(contact: Contact) {
            Log.d("TEST","contact ${contact}")
            textContactNumber.text = contact.tel
            textContactName.text = contact.name
            if (contact.avatarId !=null){
               ivAvatar.setImageBitmap(contact.avatarId)
            }
            ivCallContact.setOnClickListener {
                onItemClickListener.onItemClickCall(contact = contact)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}