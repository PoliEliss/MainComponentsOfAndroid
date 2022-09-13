package com.rorono.maincomponentsofandroid.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rorono.maincomponentsofandroid.R
import com.rorono.maincomponentsofandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

  /*  private val listContacts = listOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone._ID).toTypedArray()*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setupWithNavController(navController)


        /*binding.buttonGetContacts.setOnClickListener {
            checkPermissionReadContact()
        }*/
    }

   /* private fun checkPermissionReadContact() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                Array(1) { android.Manifest.permission.READ_CONTACTS },
                REQUEST_CODE
            )
        } else
            readContact()

    }*/

  /*  override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            readContact()
    }*/

/*    private fun readContact() {

        val from = listOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
        ).toTypedArray()

        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            listContacts, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )

        val adapter = SimpleCursorAdapter(
            this, android.R.layout.simple_list_item_2, cursor,
            from, to, 0
        )

        binding.listView.adapter = adapter
    }*/


    /*companion object {
        const val REQUEST_CODE = 200
    }*/
}

