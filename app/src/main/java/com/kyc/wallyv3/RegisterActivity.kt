package com.kyc.wallyv3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kyc.wallyv3.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(){
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)


        val sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val registerButton = binding.register
        registerButton.setOnClickListener{
            val name = binding.editTextName
            val email = binding.editTextEmail
            val password = binding.editTextPassword
            val confirmPassword = binding.editTextConfirmPassword

            //to check if password and confirm password is a match
            if(password.text.toString() != confirmPassword.text.toString()){
                confirmPassword.error = "Password mismatch"
            }

            //to store the captured information in SharedPref
            editor.apply{
                putString("name", name.text.toString())
                putString("email", email.text.toString())
                putString("password", password.text.toString())
                apply()
            }

            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}