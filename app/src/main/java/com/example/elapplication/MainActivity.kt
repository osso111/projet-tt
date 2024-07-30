package com.example.elapplication

import LoginRequest
import LoginResponse
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var saveLoginSwitch: Switch
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        // Check if login info is saved
        if (sharedPreferences.getBoolean("IS_LOGGED_IN", false)) {
            navigateToHomeScreen(sharedPreferences.getString("PHONE_NUMBER", "") ?: "")
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textViewToSecondActivity: TextView = findViewById(R.id.inscription)
        textViewToSecondActivity.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        val loginButton: Button = findViewById(R.id.login_button)
        loginButton.setOnClickListener {
            login()
        }

        saveLoginSwitch = findViewById(R.id.stay_connected)

        val savedPhoneNumber = sharedPreferences.getString("PHONE_NUMBER", null)
        val savedPassword = sharedPreferences.getString("PASSWORD", null)

        if (savedPhoneNumber != null && savedPassword != null) {
            // Autofill login info if saved
            findViewById<EditText>(R.id.phone_number).setText(savedPhoneNumber)
            findViewById<EditText>(R.id.password).setText(savedPassword)
        }

        saveLoginSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            if (isChecked) {
                val phoneNumber = findViewById<EditText>(R.id.phone_number).text.toString()
                val password = findViewById<EditText>(R.id.password).text.toString()
                editor.putString("PHONE_NUMBER", phoneNumber)
                editor.putString("PASSWORD", password)
                editor.putBoolean("IS_LOGGED_IN", true)
            } else {
                editor.remove("PHONE_NUMBER")
                editor.remove("PASSWORD")
                editor.putBoolean("IS_LOGGED_IN", false)
            }
            editor.apply()
        }
    }

    private fun login() {
        val phoneNumber = findViewById<EditText>(R.id.phone_number).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        Log.d("LoginActivity", "Phone Number:$phoneNumber")
        Log.d("LoginActivity", "Password:$password")

        if (phoneNumber.isNotEmpty() && password.isNotEmpty()) {
            val apiService = RetrofitInstance.instance
            val loginRequest = LoginRequest(phone_number = phoneNumber, password = password)
            apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        val editor = sharedPreferences.edit()
                        if (saveLoginSwitch.isChecked) {
                            editor.putBoolean("IS_LOGGED_IN", true)
                        }
                        editor.apply()

                        val intent = Intent(this@MainActivity, HomeScreen::class.java).apply {
                            putExtra("phoneNumber", phoneNumber)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToHomeScreen(phoneNumber: String) {
        val intent = Intent(this, HomeScreen::class.java)
        intent.putExtra("phoneNumber", phoneNumber)
        startActivity(intent)
    }
}
