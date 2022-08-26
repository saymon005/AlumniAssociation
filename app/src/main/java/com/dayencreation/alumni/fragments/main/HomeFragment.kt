package com.dayencreation.alumni.fragments.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dayencreation.alumni.Message
import com.dayencreation.alumni.R
import com.dayencreation.alumni.adapters.MyRecyclerViewAdapter
import com.dayencreation.alumni.adapters.PostAdapter
import com.dayencreation.alumni.databinding.FragmentHomeBinding
import com.dayencreation.alumni.fragments.authentication.SignInFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        fetchPosts()
        return binding.root
    }

    private fun fetchPosts(){
        val posts = ArrayList<Post>()

        FirebaseDatabase.getInstance().reference.child("Posts").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                posts.clear()
                snapshot.children.forEach{
                    val post = it.getValue(Post::class.java)
                    posts.add(post!!)
                }
                binding.rvpPosts.adapter = PostAdapter(posts)
                binding.rvpPosts.layoutManager = LinearLayoutManager(context)
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }
}

data class Post(
    var title: String? =null,
    var description: String?=null,
    var date_time: String?=null,
    var university: String?=null,
    var department: String?=null
)
