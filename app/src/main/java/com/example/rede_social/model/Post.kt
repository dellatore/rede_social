package com.example.rede_social.model

import android.graphics.Bitmap
import java.util.Date

class Post (private val descricao: String, private val foto: Bitmap, private val endereco: String, private val dataCriacao: Date?){
    public fun getDescricao() : String{
        return descricao
    }
    public fun getFoto() : Bitmap{
        return foto
    }

    public fun geEndereco() : String{
        return endereco
    }

    public fun getDataCriacao() : Date?{
        return dataCriacao
    }
}

