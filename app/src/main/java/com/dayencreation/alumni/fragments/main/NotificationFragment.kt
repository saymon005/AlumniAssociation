package com.dayencreation.alumni.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dayencreation.alumni.R
import com.dayencreation.alumni.adapters.ChatAdapter
import com.dayencreation.alumni.adapters.NotificationAdapter
import com.dayencreation.alumni.adapters.PostAdapter
import com.dayencreation.alumni.databinding.FragmentNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationFragment : Fragment() {

    private lateinit var binding : FragmentNotificationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        fetchNotifications()
        return binding.root
    }

    private fun fetchNotifications(){
        val notifications = ArrayList<PostNotification>()

        val notificationAdapter = NotificationAdapter(notifications)

        FirebaseDatabase.getInstance().reference.child("Posts").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notifications.clear()
                snapshot.children.forEach { ds ->
                    notifications.add(PostNotification(ds.child("title").value.toString(), ds.child("date_time").value.toString()))
                }
                notifications.asReversed()
                binding.rvNotification.adapter = notificationAdapter
                //linearLayoutManager.reverseLayout = true
                //linearLayoutManager.stackFromEnd = true
                binding.rvNotification.layoutManager = LinearLayoutManager(context)
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }
}

data class PostNotification(
    var title: String? =null,
    var date: String?=null
)