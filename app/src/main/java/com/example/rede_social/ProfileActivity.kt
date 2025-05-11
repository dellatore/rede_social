package com.example.rede_social

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.rede_social.adapter.Base64Converter
import com.example.rede_social.databinding.ActivityHomeBinding
import com.example.rede_social.databinding.ActivityPostBinding
import com.example.rede_social.databinding.ActivityProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


class ProfileActivity: AppCompatActivity() {


    private lateinit var binding: ActivityProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val galeria = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()) {
                uri ->
            if (uri != null) {
                binding.imageView.setImageURI(uri)
            } else {
                Toast.makeText(this, "Nenhuma foto selecionada", Toast.LENGTH_LONG).show()
            }
        }
        binding.alterarFoto.setOnClickListener {
            galeria.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }


        binding.saveButton.setOnClickListener{
            val firebaseAuth = FirebaseAuth.getInstance()
            if (firebaseAuth.currentUser != null){
                val email = firebaseAuth.currentUser!!.email.toString()
                val username = binding.nomeUsuario.text.toString()
                val nomeCompleto = binding.nomeCompletoUsuario.text.toString()
                val fotoPerfilString = Base64Converter.drawableToString(binding.imageView.
                drawable)
                val db = Firebase.firestore
                val dados = hashMapOf(
                    "nomeCompleto" to nomeCompleto,
                    "username" to username,
                    "fotoPerfil" to fotoPerfilString
                )
                db.collection("usuarios").document(email)
                    .set(dados)
                    .addOnSuccessListener {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
            }
        }
    }
}

