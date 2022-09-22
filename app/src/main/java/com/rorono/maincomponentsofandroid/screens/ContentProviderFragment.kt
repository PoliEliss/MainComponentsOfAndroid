package com.rorono.maincomponentsofandroid.screens

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import com.rorono.maincomponentsofandroid.models.Contact
import com.rorono.maincomponentsofandroid.R
import com.rorono.maincomponentsofandroid.adapter.OnIemClickListener
import com.rorono.maincomponentsofandroid.adapter.PhoneNumbersAdapter
import com.rorono.maincomponentsofandroid.databinding.FragmentContentProviderBinding
import com.rorono.maincomponentsofandroid.utils.BaseViewBindingFragment


class ContentProviderFragment :
    BaseViewBindingFragment<FragmentContentProviderBinding>(FragmentContentProviderBinding::inflate) {
    private var adapter = PhoneNumbersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewContactList.adapter = adapter
        binding.buttonGetContacts.setOnClickListener {
            checkPermissionReadContact()
        }

        binding.buttonWriteContacts.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_contentProviderFragment_to_addContactNumberFragment)
        }
        adapter.setOnListener(object : OnIemClickListener {
            override fun onItemClickCall(contact: Contact) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(getString(R.string.tel) + contact.tel))
                startActivity(intent)
            }
        })
    }

    private fun checkPermissionReadContact() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                Array(1) { android.Manifest.permission.READ_CONTACTS },
                REQUEST_CODE_READ_CONTACTS
            )
        } else
            adapter.submitList(getContacts())
    }

    private fun getContacts(): List<Contact> {
        val listContacts = mutableListOf<Contact>()
        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val name =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val contact = Contact(name, number)
                listContacts.add(contact)
            }
        }
        cursor?.close()
        return listContacts
    }
    companion object {
        const val REQUEST_CODE_READ_CONTACTS = 200
    }
}