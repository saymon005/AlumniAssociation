package com.dayencreation.alumni.fragments.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dayencreation.alumni.CommonFunctionContainer
import com.dayencreation.alumni.MainActivity
import com.dayencreation.alumni.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        binding.signinbtn.setOnClickListener(listener)
        binding.createAccountTxt.setOnClickListener(listener)
        binding.forgotPassTxt.setOnClickListener(listener)
        return binding.root
    }

    private val listener = View.OnClickListener{
        when(it.id){
            binding.signinbtn.id -> signIn()
            binding.createAccountTxt.id -> {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace((view?.parent as ViewGroup).id, RegistrationFragment())
                    ?.addToBackStack(null)
                    ?.commit()
            }
            binding.forgotPassTxt.id -> {

            }
        }
    }

    private fun signIn(){
        if(CommonFunctionContainer().checkEmptyField(arrayListOf(binding.email, binding.pass)))
            return

        FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.email.text.toString().replace("\\s".toRegex(), ""), binding.pass.text.toString()).addOnCompleteListener{
            if (!it.isSuccessful) return@addOnCompleteListener
            checkValidity()
        }.addOnFailureListener{
            Toast.makeText(activity, "Failed to sign in: ${it.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun checkValidity(){
        val uid = FirebaseAuth.getInstance().uid?:""

        FirebaseDatabase.getInstance().getReference("Users/$uid/RegistrationInfo").get().addOnSuccessListener {
            if (it.exists()){
                if(it.child("valid").value.toString() == "1")
                    loadUser()
                else{
                    Toast.makeText(activity,"Admin approval is in progress. Please wait. Thank you for your patience", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                }
            }
            else Toast.makeText(activity,"User doesn't exist", Toast.LENGTH_LONG).show()
        }.addOnFailureListener{
            Toast.makeText(activity,"Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUser(){
        Toast.makeText(activity, "Logged in successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }
}