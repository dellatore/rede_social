package com.example.rede_social

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.rede_social.adapter.Base64Converter
import com.example.rede_social.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var selectedImageBitmap: Bitmap? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        carregarDadosPerfil()

        val galeria = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()) {
                uri ->
            if (uri != null) {
                binding.imageViewProfile.setImageURI(uri)
            } else {
                Toast.makeText(this, "Nenhuma foto selecionada", Toast.LENGTH_LONG).show()
            }
        }


        binding.buttonSelectImage.setOnClickListener {
            galeria.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.buttonSaveProfile.setOnClickListener {
            val nome = binding.editNome.text.toString()
            val email = binding.editEmail.text.toString()
            val user = firebaseAuth.currentUser

            if (user != null) {
                val uid = user.uid
                val updates = mutableMapOf<String, Any>()

                updates["nome"] = nome
                updates["email"] = email
                updates["fotoPerfil"] = Base64Converter.drawableToString(binding.imageViewProfile.
                drawable)


                db.collection("usuarios").document(uid)
                    .update(updates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Perfil atualizado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao atualizar perfil", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun carregarDadosPerfil() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val uid = user.uid
            db.collection("usuarios").document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        binding.editNome.setText(doc.getString("nomeCompleto") ?: "")
                        binding.editEmail.setText(doc.getString("email") ?: "")
                        val fotoBase64 = doc.getString("fotoPerfil")
                        if (!fotoBase64.isNullOrEmpty()) {
                            val bitmap = Base64Converter.stringToBitmap(fotoBase64)
                            binding.imageViewProfile.setImageBitmap(bitmap)
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao carregar perfil", Toast.LENGTH_SHORT).show()
                }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                selectedImageBitmap = bitmap
                binding.imageViewProfile.setImageBitmap(bitmap)
            }
        }
    }
}
