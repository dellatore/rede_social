package com.example.rede_social

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rede_social.databinding.ActivitySinginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivitySinginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configListeners()
    }

    private fun configListeners() {
        binding.registerButton.setOnClickListener {
            criarUsuario()
        }
    }

    private fun criarUsuario() {
        val email = binding.emailEditText.text.toString().trim()
        val senha = binding.passwordEditText.text.toString().trim()
        val confirmacaoSenha = binding.confirmAsswordEditText.text.toString().trim()
        val nomeCompleto = binding.nomeCompletoUsuario.text.toString().trim()

        if (email.isNotEmpty() && senha.isNotEmpty() && confirmacaoSenha.isNotEmpty()) {
            if (senha == confirmacaoSenha) {
                firebaseAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = firebaseAuth.currentUser?.uid
                            if (userId != null) {
                                salvarUsuarioNoFirestore(userId, email, nomeCompleto)
                            }
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "As senhas estão diferentes!", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_LONG).show()
        }
    }

    private fun salvarUsuarioNoFirestore(userId: String, email: String, nomeCompleto: String) {
        val db = Firebase.firestore
        val usuarioMap = hashMapOf(
            "email" to email,
            "nomeCompleto" to nomeCompleto,
            "fotoPerfil" to ""
        )

        db.collection("usuarios").document(userId).set(usuarioMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Usuário salvo com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar usuário: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}
