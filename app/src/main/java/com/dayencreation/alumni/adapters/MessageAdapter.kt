package com.dayencreation.alumni.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dayencreation.alumni.Message
import com.dayencreation.alumni.databinding.ReceiveBinding
import com.dayencreation.alumni.databinding.SentBinding
import com.google.firebase.auth.FirebaseAuth


class MessageAdapter(data : ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val messageList : ArrayList<Message> = data
    private lateinit var context: Context

    private val ITEM_RECEIVE = 1
    private val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context

        return if (viewType == 1) {
            ReceiveViewHolder(ReceiveBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            SentViewHolder(SentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            //do the staff for sent view holder
            (holder as SentViewHolder).sentMessage.text = currentMessage.message
        } else {
            //do staff for receive view holder
            (holder as ReceiveViewHolder).receiveMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size

    }

    inner class SentViewHolder(sentBinding: SentBinding) : RecyclerView.ViewHolder(sentBinding.root) {
        val sentMessage: TextView = sentBinding.txtSentMessage
    }

    inner class ReceiveViewHolder(receiveBinding: ReceiveBinding) : RecyclerView.ViewHolder(receiveBinding.root) {
        val receiveMessage: TextView = receiveBinding.txtReceiveMessage
    }

}
