package com.kyc.wallyv3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kyc.wallyv3.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        val sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE)

        val loginButton = binding.buttonLogin
        loginButton.setOnClickListener{
            val emailSharedPref = sharedPref.getString("email", null)
            val passwordSharedPref = sharedPref.getString("password", null)

            val email = binding.editTextEmail
            val password = binding.editTextPassword

            if(emailSharedPref == email.text.toString() && passwordSharedPref == password.text.toString()){
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
            }else{
                password.error = "Incorrect Username or password"
            }
        }
    }
}