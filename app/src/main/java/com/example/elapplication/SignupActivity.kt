package com.example.elapplication

import SignupRequest
import SignupResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signupButton: Button = findViewById(R.id.signup_button)
        signupButton.setOnClickListener {
            signup()
        }
    }

    private fun signup() {
        val cin = findViewById<EditText>(R.id.CIN).text.toString()
        val username = findViewById<EditText>(R.id.username).text.toString()
        val phoneNumber = findViewById<EditText>(R.id.phone_number).text.toString()
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        Log.d("SignupActivity", "CIN:$cin")
        Log.d("SignupActivity", "Username:$username")
        Log.d("SignupActivity", "Phone Number:$phoneNumber")
        Log.d("SignupActivity", "Email:$email")
        Log.d("SignupActivity", "Password:$password")

        if (cin.isNotEmpty() && username.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            val apiService = RetrofitInstance.instance
            val signupRequest = SignupRequest(cin, username, phoneNumber, email, password)
            apiService.signup(signupRequest).enqueue(object : Callback<SignupResponse> {
                override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@SignupActivity, "Signup successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignupActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignupActivity, "Signup failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    Toast.makeText(this@SignupActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
