package com.example.elapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeScreen : AppCompatActivity() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        val phoneNumber = intent.getStringExtra("phoneNumber")
        sharedViewModel.phoneNumber = phoneNumber

        val pnumber = findViewById<TextView>(R.id.phone_number_text)
        pnumber.text = phoneNumber

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_payment -> selectedFragment = PaymentFragment()
                R.id.navigation_history -> selectedFragment = HistoryFragment()
                R.id.navigation_offers -> selectedFragment = OffersFragment()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, it).commit()
            }
            true
        }

        // Set the default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PaymentFragment()).commit()
        }
    }
}
