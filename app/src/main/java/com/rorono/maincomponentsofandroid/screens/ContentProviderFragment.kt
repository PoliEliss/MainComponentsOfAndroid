package com.rorono.maincomponentsofandroid.screens

import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.rorono.maincomponentsofandroid.R
import com.rorono.maincomponentsofandroid.adapter.OnIemClickListener
import com.rorono.maincomponentsofandroid.adapter.PhoneNumbersAdapter
import com.rorono.maincomponentsofandroid.databinding.FragmentContentProviderBinding
import com.rorono.maincomponentsofandroid.models.Contact
import com.rorono.maincomponentsofandroid.utils.BaseViewBindingFragment
import com.rorono.maincomponentsofandroid.viewmodel.MainViewModel
import com.rorono.maincomponentsofandroid.viewmodel.MainViewModelFactory


class ContentProviderFragment :
    BaseViewBindingFragment<FragmentContentProviderBinding>(FragmentContentProviderBinding::inflate) {
    private var adapter = PhoneNumbersAdapter()

    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MainViewModelFactory()
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]
        binding.recyclerViewContactList.adapter = adapter

        viewModel.listContact.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.buttonGetContacts.setOnClickListener {
            checkPermissionReadContact()
        }

        binding.buttonWriteContacts.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_contentProviderFragment_to_addContactNumberFragment)
        }
        adapter.setOnListener(object : OnIemClickListener {
            override fun onItemClickCall(contact: Contact) {
                val intent =
                    Intent(Intent.ACTION_DIAL, Uri.parse(getString(R.string.tel) + contact.tel))
                startActivity(intent)
            }
        })
    }

    private fun checkPermissionReadContact() {
        if (ContextCompat.checkSelfPermission(
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
            getContacts()
    }

    private fun getContacts() {
        val listContacts = mutableListOf<Contact>()
        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )
        cursor?.let {
            var avatar: Bitmap? = null
            Log.d("TEST","it ${it.count}")
            while (it.moveToNext()) {
                val name =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                Log.d("TEST","name $name")

                val number =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                /*val type = it.getInt(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE))
                if (type == ContactsContract.CommonDataKinds.Phone.TYPE_HOME){
                    Log.d("TEST","Home type ${number} $type")
                }*/


                val avatar_uri = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                if (avatar_uri !=null){
                    avatar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap( ImageDecoder.createSource(requireActivity().contentResolver,Uri.parse(avatar_uri)))
                    } else {
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,Uri.parse(avatar_uri))
                    }
                }
                val contact = Contact(if (name == number) "No name" else name, number, avatarId = avatar)
                listContacts.add(contact)
                avatar = null
            }
        }
        cursor?.close()
         viewModel.getContacts(listContacts)
    }

    companion object {
        const val REQUEST_CODE_READ_CONTACTS = 200
    }
}