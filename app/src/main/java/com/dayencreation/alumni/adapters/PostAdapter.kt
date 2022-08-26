package com.dayencreation.alumni.adapters

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dayencreation.alumni.R
import com.dayencreation.alumni.databinding.PostRowBinding
import com.dayencreation.alumni.databinding.RecyclerViewRowBinding
import com.dayencreation.alumni.fragments.main.Post
import com.google.firebase.database.FirebaseDatabase


class PostAdapter(data : ArrayList<Post>): RecyclerView.Adapter<PostAdapter.MyViewHolder>(){

    private val posts : ArrayList<Post> = data
    private lateinit var _binding: PostRowBinding
    private lateinit var uid : String
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        return MyViewHolder(PostRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("BindViewTest ::::::::: ", "InsideBindView")
        _binding = holder.binding
        _binding.postTitle.text = posts[position].title.toString()
        _binding.postDateTime.text = posts[position].date_time.toString()
        _binding.postDescription.text = posts[position].description.toString()

        var privacy = "Public"
        if(posts[position].university != ""){
            privacy = if(posts[position].department == "")
                posts[position].university.toString()
            else
                posts[position].university.toString() + ", " + posts[position].department.toString()
        }

        _binding.postPrivacy.text = privacy
    }

    override fun getItemCount(): Int {
        return posts.count()
    }

    inner class MyViewHolder(val binding : PostRowBinding) : RecyclerView.ViewHolder(binding.root)
}