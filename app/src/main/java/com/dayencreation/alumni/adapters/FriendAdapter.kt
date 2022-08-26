package com.dayencreation.alumni.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dayencreation.alumni.InboxActivity
import com.dayencreation.alumni.ProfileHolderActivity
import com.dayencreation.alumni.databinding.FriendRowBinding
import com.dayencreation.alumni.databinding.NotificationRowBinding
import com.dayencreation.alumni.fragments.main.FriendReqFragment
import com.dayencreation.alumni.fragments.main.PostNotification
import com.google.firebase.database.FirebaseDatabase

class FriendAdapter  (data : ArrayList<Friend>): RecyclerView.Adapter<FriendAdapter.FriendViewHolder>(){

    private val friends : ArrayList<Friend> = data
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        context = parent.context
        return FriendViewHolder(FriendRowBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.binding.friendNameTxt.text = friends[position].name.toString()
        holder.binding.root.setOnClickListener {
            val intent= Intent(context, ProfileHolderActivity::class.java)

            intent.putExtra("uid", friends[position].uid.toString())
            intent.putExtra("req", "0")

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return friends.count()
    }

    inner class FriendViewHolder(val binding : FriendRowBinding) : RecyclerView.ViewHolder(binding.root)
}

data class Friend(
    var name: String? =null,
    var uid: String?=null
)