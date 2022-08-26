package com.dayencreation.alumni.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dayencreation.alumni.adapters.ChatAdapter
import com.dayencreation.alumni.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ChatFragment : Fragment() {

    private lateinit var binding : FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
        fetchFriends()
        return binding.root
    }

    private fun fetchFriends(){
        val friends = ArrayList<ChatInbox>()

        val uid = FirebaseAuth.getInstance().uid?:""

        val chatAdapter = ChatAdapter(friends)
        binding.rvInboxes.adapter = chatAdapter
        binding.rvInboxes.layoutManager = LinearLayoutManager(context)

        FirebaseDatabase.getInstance().getReference("Users/$uid/Friends/uids").get().addOnSuccessListener {
            friends.clear()
            it.children.forEach { ds ->
                val _uid = ds.value.toString()
                FirebaseDatabase.getInstance().getReference("Users/$_uid/PersonalInfo/fullName").get().addOnSuccessListener { nm ->
                    FirebaseDatabase.getInstance().getReference("Chats").child(_uid+uid).child("lastMsg").get().addOnSuccessListener { ls ->
                        val friend = ChatInbox(nm.value.toString(), _uid, ls.value.toString())
                        friends.add(friend)
                        chatAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}

data class ChatInbox(
    var name: String? =null,
    var uid: String?=null,
    var lastMsg:String?=null
)
