package com.dayencreation.alumni.fragments.authentication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dayencreation.alumni.CommonFunctionContainer
import com.dayencreation.alumni.MainActivity
import com.dayencreation.alumni.R
import com.dayencreation.alumni.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.lang.StringBuilder

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        binding.regSignIn.setOnClickListener(listener)
        binding.signupbtn.setOnClickListener(listener)
        return binding.root
    }

    private val listener = View.OnClickListener{
        when(it.id){
            binding.regSignIn.id -> {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace((view?.parent as ViewGroup).id, SignInFragment())
                    ?.commit()
            }
            binding.signupbtn.id -> signUp()
        }
    }
    private fun signUp(){
        val fields : ArrayList<EditText> = ArrayList()
        fields.add(binding.regFullName)
        fields.add(binding.regUniName)
        fields.add(binding.regDeptName)
        fields.add(binding.regUniId)
        fields.add(binding.regGradYear)
        fields.add(binding.regSession)
        fields.add(binding.regEmail)
        fields.add(binding.regConfirmPass)
        fields.add(binding.regPass)
        if(CommonFunctionContainer().checkEmptyField(fields))
            return
        if(binding.regPass.text.toString() != binding.regConfirmPass.text.toString())
        {
            binding.regConfirmPass.error = "Password Mismatch"
            return
        }
        performRegister()
    }

    private fun performRegister() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            binding.regEmail.text.toString().replace("\\s".toRegex(), ""),
            binding.regPass.text.toString()
        ).addOnCompleteListener {
            if (!it.isSuccessful) return@addOnCompleteListener

        }.addOnSuccessListener {
            saveData()
        }
        .addOnFailureListener {
            Toast.makeText(activity, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun saveData(){
        val uid = FirebaseAuth.getInstance().uid?:""

        val database = FirebaseDatabase.getInstance().getReference("/Users/$uid")

        //database.setValue(Admin("0"))

        database.child("Friends").child("friendCount").setValue("0")
        //database.child("Friends/uids").child("0").setValue("")

        database.child("RegistrationInfo").setValue(RegUser(binding.regEmail.text.toString(), "0")).addOnSuccessListener {

        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
        }

        val user = UserInfo(binding.regFullName.text.toString(), binding.regUniName.text.toString(),
            binding.regDeptName.text.toString(), binding.regUniId.text.toString(), binding.regGradYear.text.toString(), binding.regSession.text.toString())

        database.child("PersonalInfo").setValue(user).addOnSuccessListener {

            FirebaseAuth.getInstance().signOut()
            //Toast.makeText(activity, "Successfully Registered. Wait for admin approval", Toast.LENGTH_LONG).show()
            showAlert()

        }
        .addOnFailureListener {
            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAlert(){
        val alertDialogBuilder = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setTitle("Registration Successful")
        alertDialogBuilder.setMessage("Please wait for admin approval. you'll be notified through email.")
        alertDialogBuilder.setPositiveButton("OK") { _, _ ->
            startActivity(Intent(activity, MainActivity::class.java))
        }
        alertDialogBuilder.create()?.show()
    }
}

//data class Admin(val admin : String? = null)

data class RegUser(val emailAddress : String? = null, val valid: String ? = null, val verified: String ? = null)

data class UserInfo(val fullName : String? = null,
                   val university : String? = null, val department : String? = null,
                   val universityId : String? = null, val graduationYear : String ? = null,
                   val session: String ? = null)
