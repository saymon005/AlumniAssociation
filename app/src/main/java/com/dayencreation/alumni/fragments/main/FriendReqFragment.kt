package com.dayencreation.alumni.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dayencreation.alumni.R
import com.dayencreation.alumni.adapters.Friend
import com.dayencreation.alumni.adapters.FriendAdapter
import com.dayencreation.alumni.databinding.FragmentFriendReqBinding
import com.dayencreation.alumni.databinding.FriendReqRowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendReqFragment : Fragment() {

    private lateinit var binding: FragmentFriendReqBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendReqBinding.inflate(layoutInflater)
        return binding.root
    }
    private fun fetchFriendReqs(){
        val friends = ArrayList<Friend>()

        val friendAdapter = FriendAdapter(friends)

        val uid = FirebaseAuth.getInstance().uid?:""

        val ref = FirebaseDatabase.getInstance().getReference("Users")

        ref.child(uid).child("FriendReq").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                friends.clear()
                snapshot.children.forEach { ds ->
                        friends.add(Friend(ds.value.toString(), ds.key.toString()))

                        binding.rvReqFriends.adapter = friendAdapter
                        binding.rvReqFriends.layoutManager = LinearLayoutManager(context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }
}