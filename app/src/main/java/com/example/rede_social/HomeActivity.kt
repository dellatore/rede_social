package com.example.rede_social

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rede_social.adapter.Base64Converter
import com.example.rede_social.adapter.PostAdapter
import com.example.rede_social.databinding.ActivityHomeBinding
import com.example.rede_social.model.Post
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class HomeActivity: AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivityHomeBinding
    private var lastVisibleDocument: DocumentSnapshot? = null
    private val PAGE_SIZE = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        println("entrou na home")
        configListeners()
    }

    private fun configListeners(){
        binding.buttonExit.setOnClickListener {
            logout()
        }

        binding.buttonLoad.setOnClickListener{
            val db = Firebase.firestore
            val cidadeFiltro = binding.editFiltroCidade.text.toString().trim()

            var query = if (cidadeFiltro.isNotEmpty()) {
                db.collection("posts")
                    .whereEqualTo("endereco", cidadeFiltro)
            } else {
                db.collection("posts")
            }
            query = query.limit(PAGE_SIZE.toLong())

            query.get().addOnSuccessListener { result ->

                if (!result.isEmpty) {
                    lastVisibleDocument = result.documents.last()
                }

                val posts = result.documents.mapNotNull { doc ->
                    try {
                        val imageString = doc.data!!["fotoPost"].toString()
                        val bitmap = Base64Converter.stringToBitmap(imageString)
                        val descricao = doc.data!!["descricao"].toString()
                        val endereco = doc.data!!["endereco"].toString()
                        Post(descricao, bitmap,endereco)
                    } catch (e: Exception) {
                        null
                    }
                }

                val adapter = PostAdapter(posts.toTypedArray())
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                binding.recyclerView.adapter = adapter
            }.addOnFailureListener {
                Toast.makeText(this, "Erro ao buscar posts", Toast.LENGTH_SHORT).show()
            }
        }


        binding.buttonLoadMore.setOnClickListener {
            var ultimoTimestamp: Timestamp? = null
            val db = Firebase.firestore
            fun carregarLogs() {
                var query = db.collection("logs")
                    .orderBy("data", Query.Direction.DESCENDING)
                    .limit(5)
                if (ultimoTimestamp != null) {
                    query = query.startAfter(ultimoTimestamp!!)
                }
                query.get()
                    .addOnSuccessListener { result ->
                        if (!result.isEmpty) {
                            ultimoTimestamp =
                                result.documents.last().getTimestamp("data")
                            val posts = result.documents.mapNotNull { doc ->
                                try {
                                    val imageString = doc.data!!["fotoPost"].toString()
                                    val bitmap = Base64Converter.stringToBitmap(imageString)
                                    val descricao = doc.data!!["descricao"].toString()
                                    val endereco = doc.data!!["endereco"].toString()
                                    Post(descricao, bitmap,endereco)
                                } catch (e: Exception) {
                                    null
                                }
                            }
                            val adapter = PostAdapter(posts.toTypedArray())
                        }

                    }
            }
        }

        binding.buttonEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        binding.buttonAdd.setOnClickListener {
            startActivity(Intent(this, AddPostActivity::class.java))
        }
    }


    private fun logout(){
        firebaseAuth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
