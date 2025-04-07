package com.example.rede_social

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rede_social.databinding.ActivityHomeBinding
import com.example.rede_social.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity: AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        println("entrou na home")
        configListeners()
    }

    private fun configListeners(){
        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout(){
        firebaseAuth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
