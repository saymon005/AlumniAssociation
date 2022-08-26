package com.dayencreation.alumni.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dayencreation.alumni.adapters.Friend
import com.dayencreation.alumni.adapters.FriendAdapter
import com.dayencreation.alumni.databinding.FragmentFriendsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendsFragment : Fragment() {

    private lateinit var binding : FragmentFriendsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(layoutInflater)
        fetchFriends()

        binding.seeReqBtn.setOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace((view?.parent as ViewGroup).id, FriendReqFragment())
                ?.commit()
        }
        return binding.root
    }

    private fun fetchFriends(){
        val friends = ArrayList<Friend>()

        val friendAdapter = FriendAdapter(friends)

        val uid = FirebaseAuth.getInstance().uid?:""

        val ref = FirebaseDatabase.getInstance().getReference("Users")

        ref.child(uid).child("FriendReq").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var counter = 0
                snapshot.children.forEach { _ ->
                    counter++
                }
                binding.reqCountTxt.text = counter.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })

        ref.child(uid).child("Friends").child("uids").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                friends.clear()
                snapshot.children.forEach { ds ->
                    val _uid = ds.value.toString()
                    ref.child(_uid).child("PersonalInfo").child("fullName").get().addOnSuccessListener {

                        friends.add(Friend(it.value.toString(), _uid))

                        binding.rvFriends.adapter = friendAdapter
                        binding.rvFriends.layoutManager = LinearLayoutManager(context)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }
}