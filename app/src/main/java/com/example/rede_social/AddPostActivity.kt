package com.example.rede_social

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.rede_social.adapter.Base64Converter
import com.example.rede_social.adapter.LocalizacaoHelper
import com.example.rede_social.databinding.ActivityAddPostBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.io.IOException
import java.util.Locale


class AddPostActivity : AppCompatActivity(), LocalizacaoHelper.Callback {

    private lateinit var binding: ActivityAddPostBinding
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val galeria = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()) {
                uri ->
            if (uri != null) {
                binding.imagePreview.setImageURI(uri)
            } else {
                Toast.makeText(this, "Nenhuma foto selecionada", Toast.LENGTH_LONG).show()
            }
        }
        binding.buttonSelectImage.setOnClickListener {
            galeria.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.buttonPost.setOnClickListener {
            val enderecoDigitado = binding.editEndereco.text.toString()
            if (enderecoDigitado.isEmpty()){
                obterLocalizacaoAutomaticamente()
            }else{
                salvarPost(enderecoDigitado)
            }


        }
    }

    private fun obterLocalizacaoAutomaticamente() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            val localizacaoHelper = LocalizacaoHelper(this)
            localizacaoHelper.obterLocalizacaoAtual(this)
        }
    }

    override fun onLocalizacaoRecebida(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val endereco = try {
            val lista = geocoder.getFromLocation(latitude, longitude, 1)
            if (lista != null && lista.isNotEmpty()) {
                val enderecoObj = lista[0]
                val cidade = enderecoObj.locality ?: "Araraquara"
                cidade.trim()
            } else {
                "Endereço não encontrado"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            "Erro ao obter endereço"
        }

        salvarPost(endereco)
    }



    private fun salvarPost( endereco: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
            val descricao = binding.editDescricao.text.toString()
            val fotoPost = Base64Converter.drawableToString(
                binding.imagePreview.drawable
            )
            val db = Firebase.firestore
            val dados = hashMapOf(
                "descricao" to descricao,
                "fotoPost" to fotoPost,
                "endereco" to endereco
            )
            db.collection("posts")
                .add(dados)
                .addOnSuccessListener {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
        }
    }

    override fun onErro(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            obterLocalizacaoAutomaticamente()
        } else {
            Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show()
        }
    }
}
