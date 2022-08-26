package com.dayencreation.alumni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dayencreation.alumni.adapters.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InboxActivity : AppCompatActivity() {

    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        Log.d("Getting name ::::: ", name.toString() )

        val sentButton : ImageView = findViewById(R.id.sentButton)
        val titletxt : TextView = findViewById(R.id.receiverTitle)
        val chatRecyclerView : RecyclerView = findViewById(R.id.chatRecyclerView)
        val messageBox : TextView = findViewById(R.id.messageBox)

        val senderUid = FirebaseAuth.getInstance().uid?:""
        val mDbRef = FirebaseDatabase.getInstance().reference

        val senderRoom = receiverUid + senderUid
        val receiverRoom = senderUid + receiverUid

        val messageList = ArrayList<Message>()


        titletxt.text = name.toString()

        messageAdapter = MessageAdapter(messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        mDbRef.child("Chats").child(senderRoom).child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    Log.d("Snapshot :::::: ", snapshot.toString())
                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        sentButton.setOnClickListener {
            Log.d("clicked :::: ", "yes")

            val lastMsg = messageBox.text.toString()
            val messageObject=Message(lastMsg, senderUid)

            mDbRef.child("Chats").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {

                    mDbRef.child("Chats").child(receiverRoom).child("messages").push()
                        .setValue(messageObject)
                    mDbRef.child("Chats").child(senderRoom).child("lastMsg").setValue(lastMsg.toString())
                    mDbRef.child("Chats").child(receiverRoom).child("lastMsg").push().setValue(lastMsg.toString())
                }

            messageBox.text = ""
        }
    }
}

data class Message(
    var message: String? =null,
    var senderId: String?=null
)
