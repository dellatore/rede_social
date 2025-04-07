package com.example.rede_social

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rede_social.databinding.ActivityHomeBinding
import com.example.rede_social.databinding.ActivityLoginBinding
import com.example.rede_social.databinding.ActivitySinginBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity: AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivitySinginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configListeners()

    }

    private fun configListeners(){

        binding.registerButton.setOnClickListener {
            criarUsuario()
        }

    }

    fun criarUsuario(){
        val email = binding.emailEditText.text.toString()
        val senha = binding.passwordEditText.text.toString()
        val confirmacaoSenha = binding.confirmAsswordEditText.text.toString()

        if (email.isNotEmpty() && senha.isNotEmpty() && confirmacaoSenha.isNotEmpty()){

            if (senha.equals(confirmacaoSenha)) {
                firebaseAuth
                    .createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        }

                    }
            }else{
                Toast.makeText(this, "Os campos senha e confirmacao estao diferentes!!", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, "Possui campos vazios!!", Toast.LENGTH_LONG).show()
        }

    }
}