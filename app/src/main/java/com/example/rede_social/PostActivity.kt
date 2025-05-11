package com.example.rede_social

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.rede_social.databinding.ActivityLoginBinding
import com.example.rede_social.databinding.ActivityPostBinding
import com.google.firebase.auth.FirebaseAuth

class PostActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configListeners()
    }

    private fun configListeners(){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.1.66:8080/posts/1"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                binding.textView.text = response.getString("descricao")
                val queue = Volley.newRequestQueue(this)
                val urlImage = "http://192.168.1.66:8080/images/" +
                        response.getString("foto")
                val imageRequest = ImageRequest(urlImage,
                    { response ->
                        binding.imageView.setImageBitmap(response)
                    },
                    0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                    { error ->
                        error.printStackTrace()
                    })
                queue.add(imageRequest)
            },
            { error ->
                error.printStackTrace()
            }
        )
        queue.add(jsonRequest)
    }

}