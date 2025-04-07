package com.example.rede_social

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rede_social.databinding.ActivityLoginBinding
import com.example.rede_social.ui.theme.Rede_socialTheme
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityLoginBinding.inflate(layoutInflater)
         setContentView(binding.root)

         configListeners()
    }

    private fun configListeners(){
        binding.loginButton.setOnClickListener {
            cadastrar()
        }

        binding.registerButton.setOnClickListener {
            criarUsuario()
        }
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun criarUsuario(){
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    private fun cadastrar(){

        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Erro no login", Toast.LENGTH_LONG).show()
                }
            }
    }

}
