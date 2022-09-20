package com.rorono.maincomponentsofandroid.screens

import android.Manifest
import android.app.AlertDialog
import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.OperationApplicationException
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.RemoteException
import android.provider.ContactsContract
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.rorono.maincomponentsofandroid.R
import com.rorono.maincomponentsofandroid.databinding.FragmentAddContactNumberBinding
import com.rorono.maincomponentsofandroid.utils.BaseViewBindingFragment


class AddContactNumberFragment :
    BaseViewBindingFragment<FragmentAddContactNumberBinding>(FragmentAddContactNumberBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameFocusListener()
        phoneFocusListener()
        binding.buttonCancel.setOnClickListener {
            moveAnotherFragment(view)
        }
        binding.buttonSave.setOnClickListener {
            if (isWriteContactPermissionEnabled() && validData()) {
                addContact()
                moveAnotherFragment(view)
            } else {
                requestPermissionWriteContacts()
                invalidForm()
            }
        }
    }

    private fun resetForm() {
        binding.edtTelNumber.text = null
        binding.edtNameContact.text = null
    }

    private fun invalidForm(){
        var message = ""
        if (binding.textInputLayoutNameContact.helperText != null){
            message += binding.textInputLayoutNameContact.helperText
        }
        if (binding.textInputLayoutTelNumber.helperText != null){
            message += binding.textInputLayoutTelNumber.helperText
        }
        AlertDialog.Builder(requireActivity())
            .setTitle("InvalidForm")
            .setMessage(message)
            .setPositiveButton("Okay"){
                _,_->

            }.show()
    }

    private fun validData(): Boolean {
       val validNumber =binding.textInputLayoutTelNumber.helperText == null
       val validName =binding.textInputLayoutNameContact.helperText == null
        val validEditText = binding.edtNameContact.text
        val validNumberEditText = binding.edtTelNumber.text
        if (validEditText != null) {
            if (validNumberEditText != null) {
                if (validNumber && validName && validEditText.isNotEmpty() && validNumberEditText.isNotEmpty())
                    return true
            }
        }
        return false
    }

    private fun isWriteContactPermissionEnabled(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun moveAnotherFragment(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_addContactNumberFragment_to_contentProviderFragment)
    }

    private fun requestPermissionWriteContacts() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            Array(1) { android.Manifest.permission.WRITE_CONTACTS },
            REQUEST_CODE_WRITE_CONTACT
        )
    }

    private fun nameFocusListener() {
        binding.edtNameContact.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.textInputLayoutNameContact.helperText = validName()
            }
        }
    }

    private fun validName(): String? {
        val name = binding.edtNameContact.text.toString()
        return if (name.isNotEmpty() && name.isNotBlank()) {
            null
        } else
            getString(R.string.fiels_not_empty)
    }

    private fun phoneFocusListener() {
        binding.edtTelNumber.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.textInputLayoutTelNumber.helperText = validPhoneNumber()
            }
        }
    }

    private fun validPhoneNumber(): String? {
        val phoneNumber = binding.edtTelNumber.text.toString()
        if (!phoneNumber.matches(".*[0-9].*".toRegex())) {
            return "Номер должен содержать только цифры"
        }
        if (phoneNumber.length != 11) {
            return "Номер должен содрежать 11 цифр"
        }
        return null
    }

    private fun addContact() {
        val arrayListContentProviderOperation = ArrayList<ContentProviderOperation>()
        arrayListContentProviderOperation.add(
            ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI
            )
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )
        //adding name
        arrayListContentProviderOperation.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(
                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                    binding.edtNameContact.text?.trim().toString()
                )
                .build()
        )
        //adding Number
        arrayListContentProviderOperation.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    binding.edtTelNumber.text?.trim().toString()
                )
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                )
                .build()
        )
        try {
            requireActivity().contentResolver.applyBatch(
                ContactsContract.AUTHORITY,
                arrayListContentProviderOperation
            )
        } catch (e: OperationApplicationException) {
            e.printStackTrace()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
        resetForm()
    }

    companion object {
        const val REQUEST_CODE_WRITE_CONTACT = 200
    }
}