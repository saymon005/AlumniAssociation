package com.dayencreation.alumni.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dayencreation.alumni.InboxActivity
import com.dayencreation.alumni.databinding.InboxRowBinding
import com.dayencreation.alumni.databinding.NotificationRowBinding
import com.dayencreation.alumni.fragments.main.PostNotification

class NotificationAdapter (data : ArrayList<PostNotification>): RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){

    private val notifications : ArrayList<PostNotification> = data
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        context = parent.context
        return NotificationViewHolder(NotificationRowBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.binding.notiTitleTxt.text = this.notifications[position].title.toString()
        holder.binding.notiTimeTxt.text = this.notifications[position].date.toString()
    }

    override fun getItemCount(): Int {
        return notifications.count()
    }

    inner class NotificationViewHolder(val binding : NotificationRowBinding) : RecyclerView.ViewHolder(binding.root)
}