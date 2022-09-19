package com.rorono.maincomponentsofandroid.screens

import android.Manifest
import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.OperationApplicationException
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.RemoteException
import android.provider.ContactsContract
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.rorono.maincomponentsofandroid.R
import com.rorono.maincomponentsofandroid.databinding.FragmentAddContactNumberBinding
import com.rorono.maincomponentsofandroid.utils.BaseViewBindingFragment


class AddContactNumberFragment :
    BaseViewBindingFragment<FragmentAddContactNumberBinding>(FragmentAddContactNumberBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCancel.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_addContactNumberFragment_to_contentProviderFragment)
        }

        binding.buttonSave.setOnClickListener {
            addContact()
            Navigation.findNavController(view)
                .navigate(R.id.action_addContactNumberFragment_to_contentProviderFragment)
        }
    }

    private fun isWriteContactPermissionEnabled(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun addContact() {
       val arrayListContentProviderOperation = ArrayList<ContentProviderOperation>()
        arrayListContentProviderOperation.add(ContentProviderOperation.newInsert(
            ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME,null)
            .build())

        //adding name
        arrayListContentProviderOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
            .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, binding.edtNameContact.text.toString())
            .build())

        //adding Number
        arrayListContentProviderOperation.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
            .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, binding.edtTelNumber.text.toString())
            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
            .build())


        try {
       requireActivity().contentResolver.applyBatch(ContactsContract.AUTHORITY, arrayListContentProviderOperation)
        } catch (e: OperationApplicationException) {
            e.printStackTrace()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }


    }


}