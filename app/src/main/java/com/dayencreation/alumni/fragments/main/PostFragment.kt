package com.dayencreation.alumni.fragments.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import com.dayencreation.alumni.MainActivity
import com.dayencreation.alumni.R
import com.dayencreation.alumni.databinding.FragmentPostBinding
import com.dayencreation.alumni.databinding.PostRowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding

    private var uni = ""
    private var dept = ""
    private var databaseRef = FirebaseDatabase.getInstance().getReference("University")

    private val uniArray = ArrayList<String>()
    private val deptArray = ArrayList<ArrayList<String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(layoutInflater)

        fetchUni()

        getUniDropDown()
        getDeptDropDown()

        dropDownVisibility(uni = false, dept = false)
        binding.publicRadioBtn.isChecked = true

        binding.radioBtnGrp.setOnCheckedChangeListener{ _, checkedId ->
            when (checkedId) {
                binding.publicRadioBtn.id -> {
                    uni = ""
                    dept = ""
                    dropDownVisibility(uni = false, dept = false)
                }
                binding.localRadioBtn.id -> {
                    dropDownVisibility(uni = true, dept = false)
                    dept = ""
                }
                binding.privateRadioBtn.id -> {
                    dropDownVisibility(uni = true, dept = true)
                }
            }
        }

        binding.postBtnSend.setOnClickListener{
            postIt()
        }
        return binding.root
    }

    private fun getUniDropDown() {
        binding.spinnerUni.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                uni = parent.getItemAtPosition(position).toString()
                fetchDept(position)
                Log.d("Selected ::::: ", uni)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Selected ::::: ", "notingggg")
            }
        }
    }

    private fun getDeptDropDown() {
        binding.spinnerDept.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                dept = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // another interface callback
            }
        }
    }

    private fun dropDownVisibility(uni: Boolean, dept : Boolean){
        binding.postUniDropDownTxt.isVisible = uni
        binding.postdeptDropDownTxt.isVisible = dept
        binding.spinnerUni.isVisible = uni
        binding.spinnerDept.isVisible = dept
    }

    private fun fetchUni(){
        databaseRef.get().addOnSuccessListener {
            it.children.forEach { ds ->
                val u = ds.key.toString()
                uniArray.add(u)
                val temp = ArrayList<String>()
                ds.child("Department").children.forEach{ ch ->
                    temp.add(ch.key.toString())
                }
                deptArray.add(temp)
            }
            val uniAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, uniArray) }
            uniAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerUni.adapter = uniAdapter
        }
    }

    private fun fetchDept(i : Int){
        val deptAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, deptArray[i]) }
        deptAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDept.adapter = deptAdapter
    }

    private fun postIt(){
        val currentDate = SimpleDateFormat("dd/MM/yyyy, hh:mm:ss").format(Date())
        val post = Post(
            binding.postTitleEditTxt.text.toString(),
            binding.postDescriptionEditTxt.text.toString(),
            currentDate.toString(),
            uni,
            dept
        )
        val uid = FirebaseAuth.getInstance().uid?:""
        FirebaseDatabase.getInstance().reference.child("Posts/$uid").setValue(post).addOnSuccessListener{
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}