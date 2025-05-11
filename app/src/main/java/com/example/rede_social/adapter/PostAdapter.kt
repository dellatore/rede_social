package com.example.rede_social.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rede_social.R
import com.example.rede_social.model.Post

class PostAdapter(private var posts: Array<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val imgPost: ImageView = view.findViewById(R.id.imageViewPost)
        val txtDescricao: TextView = view.findViewById(R.id.textViewDescription)
        val txtEndereco: TextView = view.findViewById(R.id.textViewEndereco)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return posts.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtDescricao.text = posts[position].getDescricao()
        holder.txtEndereco.text = posts[position].geEndereco()
        holder.imgPost.setImageBitmap(posts[position].getFoto())
    }

    fun addPosts(novos: List<Post>) {
        val atual = posts.toMutableList()
        atual.addAll(novos)
        posts = atual.toTypedArray()
        notifyDataSetChanged()
    }
}