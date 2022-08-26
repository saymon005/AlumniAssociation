package com.dayencreation.alumni.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dayencreation.alumni.InboxActivity
import com.dayencreation.alumni.ProfileHolderActivity
import com.dayencreation.alumni.databinding.FriendReqRowBinding
import com.dayencreation.alumni.databinding.FriendRowBinding
import com.google.firebase.database.FirebaseDatabase

class FriendReqAdapter  (data : ArrayList<Friend>): RecyclerView.Adapter<FriendReqAdapter.FriendReqViewHolder>(){

    private val friends : ArrayList<Friend> = data
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendReqViewHolder {
        context = parent.context
        return FriendReqViewHolder(FriendReqRowBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: FriendReqViewHolder, position: Int) {
        holder.binding.reqfriendNameTxt.text = friends[position].name.toString()
        holder.binding.root.setOnClickListener {
            val intent= Intent(context, ProfileHolderActivity::class.java)

            intent.putExtra("uid", friends[position].uid.toString())
            intent.putExtra("req", "1")

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return friends.count()
    }

    inner class FriendReqViewHolder(val binding : FriendReqRowBinding) : RecyclerView.ViewHolder(binding.root)
}

