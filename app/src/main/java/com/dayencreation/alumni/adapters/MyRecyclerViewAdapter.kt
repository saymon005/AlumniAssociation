package com.dayencreation.alumni.adapters

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dayencreation.alumni.R
import com.dayencreation.alumni.databinding.RecyclerViewRowBinding
import com.google.firebase.database.FirebaseDatabase


class MyRecyclerViewAdapter(data : ArrayList<ArrayList<String>>): RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>(){

    private val users : ArrayList<ArrayList<String>> = data
    private lateinit var _binding: RecyclerViewRowBinding
    private lateinit var uid : String
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        return MyViewHolder(RecyclerViewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("BindViewTest ::::::::: ", "InsideBindView")
        _binding = holder.binding

        uid = users[position][0]

        _binding.userName.text = users[position][1]
        _binding.userUniversity.text = users[position][2]
        _binding.userDepartment.text = users[position][3]
        _binding.userId.text = users[position][4]
        _binding.userGradyr.text = users[position][5]
        _binding.userSession.text = users[position][6]

        _binding.accept.setOnClickListener(listener)
        _binding.reject.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        Log.d("UserNumber ::::::::: ", users.count().toString())
        return users.count()
    }

    private val listener = View.OnClickListener{
        when(it.id){
            _binding.accept.id -> {
                showAlert(true)
            }
            _binding.reject.id -> {
                /*Firebase.auth.removeIdTokenListener {

                }
                FirebaseAuth.getInstance(fireba)
                Firebase.auth.de getuse.delete().then(function() {
                    // User deleted.
                    var ref = firebase.database().ref(
                        "users/".concat(user.uid, "/")
                    );
                    ref.remove()
                });*/
                showAlert(false)
            }
        }
    }

    private fun showAlert(accept : Boolean){
        val msg = if(accept) "accept" else "reject"
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setTitle("Confirmation Alert!")
        alertDialogBuilder.setMessage("Are you sure you want to $msg this account?")
        alertDialogBuilder.setPositiveButton("OK") { _, _ ->
            if(accept) FirebaseDatabase.getInstance().getReference("Users/$uid/RegistrationInfo/valid").setValue("1")
            else FirebaseDatabase.getInstance().getReference("Users/$uid").removeValue()
        }
        alertDialogBuilder.setNegativeButton("CANCEL"){ _, _ ->

        }
        alertDialogBuilder.create()?.show()
    }

    inner class MyViewHolder(val binding : RecyclerViewRowBinding) : RecyclerView.ViewHolder(binding.root)
}