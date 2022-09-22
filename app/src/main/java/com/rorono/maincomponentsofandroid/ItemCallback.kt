package com.rorono.maincomponentsofandroid

import androidx.recyclerview.widget.DiffUtil
import com.rorono.maincomponentsofandroid.models.Contact

class ItemCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.tel == newItem.tel
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}