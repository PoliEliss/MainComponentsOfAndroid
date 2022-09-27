package com.rorono.maincomponentsofandroid.screens

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
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

        binding.recyclerViewContactList.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    binding.buttonGetContacts.show()
                    binding.buttonWriteContacts.show()
                } else if (dy > 0) {
                    binding.buttonGetContacts.hide()
                    binding.buttonWriteContacts.hide()
                }
            }
        })

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
        val hasMapListContacts = hashMapOf<String, Contact>()
        val listContacts = mutableListOf<Contact>()
        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )
        cursor?.let {
            var avatar: Bitmap? = null
            while (it.moveToNext()) {
                val name =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val id =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
                var number: String? = null
                var homeNumber: String? = null

                when (it.getInt(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE))) {
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> {
                        number =
                            it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> {
                        homeNumber =
                            it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                }
                val avatarUri =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                if ( avatarUri != null) {
                    avatar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                Uri.parse( avatarUri)
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            Uri.parse( avatarUri)
                        )
                    }
                }
                val contact = Contact(
                    if (name == number) getString(R.string.no_name) else name,
                    number,
                    avatarId = avatar,
                    homeNumber = homeNumber,
                    id = id
                )
                listContacts.add(contact)
                avatar = null
            }
        }
        cursor?.close()
        for (i in listContacts) {
            if (hasMapListContacts.containsKey(i.id)) {
                if (i.tel != null) {
                    val changeContactMobileNumber = hasMapListContacts.getValue(i.id)
                    changeContactMobileNumber.tel = i.tel
                    hasMapListContacts[i.id] = changeContactMobileNumber
                }
                if (i.homeNumber != null) {
                    val changeContactHomeNumber = hasMapListContacts.getValue(i.id)
                    changeContactHomeNumber.homeNumber = i.homeNumber
                    hasMapListContacts[i.id] = changeContactHomeNumber
                }
            } else {
                hasMapListContacts[i.id] = i
            }
        }
        println(hasMapListContacts)
        viewModel.getContacts(listContacts = hasMapListContacts.values.toMutableList())
    }

    companion object {
        const val REQUEST_CODE_READ_CONTACTS = 200
    }
}