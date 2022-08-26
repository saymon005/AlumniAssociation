package com.dayencreation.alumni.adapters

import android.content.Context
import com.dayencreation.alumni.R
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dayencreation.alumni.InboxActivity
import com.dayencreation.alumni.databinding.InboxRowBinding
import com.dayencreation.alumni.fragments.menu.ChatInbox
import com.google.firebase.database.FirebaseDatabase


class ChatAdapter(data : ArrayList<ChatInbox>): RecyclerView.Adapter<ChatAdapter.MyViewHolder>(){

    private val friends : ArrayList<ChatInbox> = data
    private lateinit var _binding: InboxRowBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        return MyViewHolder(InboxRowBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        _binding = holder.binding

        val uid = this.friends[position].uid
        val name = this.friends[position].name

        _binding.inboxName.text = name
        _binding.recentMsg.text = this.friends[position].lastMsg

        _binding.root.setOnClickListener {
            val intent= Intent(context, InboxActivity::class.java)

            intent.putExtra("name", name)
            intent.putExtra("uid", uid)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        Log.d("Counte ::::: ", friends.count().toString())
        return friends.count()
    }

    inner class MyViewHolder(val binding : InboxRowBinding) : RecyclerView.ViewHolder(binding.root)
}