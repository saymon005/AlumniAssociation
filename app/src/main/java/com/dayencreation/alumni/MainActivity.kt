package com.dayencreation.alumni

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.dayencreation.alumni.databinding.FilterPanelBinding
import com.dayencreation.alumni.fragments.main.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navView : NavigationView

    private val uniArray = ArrayList<String>()

    private val databaseRef = FirebaseDatabase.getInstance().getReference("University")

    private lateinit var postBtn : ImageView
    private lateinit var filter : ImageView

    private lateinit var drawerlayout : DrawerLayout
    private var id : Int = R.id.home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        replaceFragment(HomeFragment())

        navView = findViewById(R.id.nav_view)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        drawerlayout = findViewById(R.id.drawerLayout)
        postBtn = findViewById(R.id.postBtn)
        //filter = findViewById(R.id.filterBtn)

        postBtn.isVisible = false

        changeAccountMenuItems(FirebaseAuth.getInstance().uid?:"" != "")
        //getUniDept()

        //if (drawerlayout.isOpen)  drawerlayout.closeDrawer(Gravity.RIGHT)

        bottomNavigationView.setOnItemSelectedListener{
            if(it.itemId != R.id.menu) id = it.itemId
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.friends -> replaceFragment(FriendsFragment())
                R.id.notification -> replaceFragment(NotificationFragment())
                R.id.menu -> {
                    drawerlayout.openDrawer(Gravity.RIGHT)

                    Handler(Looper.getMainLooper()).postDelayed({
                        //bottomNavigationView.menu.findItem(it.itemId).isChecked = false
                        bottomNavigationView.menu.findItem(id).isChecked = true
                    }, 5)
                }
            }
            true
        }

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.profile -> loadMenuItemActivity(0)
                //R.id.settings -> loadMenuItemActivity(1)
                R.id.signin -> loadMenuItemActivity(2)
                R.id.signup -> loadMenuItemActivity(3)
                R.id.adminpanel -> loadMenuItemActivity(4)
                R.id.chat -> loadMenuItemActivity(5)
                R.id.verify -> sendVerificationMail()
                R.id.signout ->{
                    FirebaseAuth.getInstance().signOut()
                    changeAccountMenuItems(false)
                }
            }
            true
        }

        postBtn.setOnClickListener{
            replaceFragment(PostFragment())
        }
        /*filter.setOnClickListener {
            filter()
        }*/
    }

    private fun filter(){

        //val popUpView = LayoutInflater.from(this).inflate(R.layout.filter_panel,null)
        val binding = FilterPanelBinding.inflate(layoutInflater)

        val nBuilder = AlertDialog.Builder(this)
            .setView(binding.root)
            .setTitle("Search Filter")
        val nAlertDialog = nBuilder.show()

        //fetchUni()

       // getUniDropDown()
        //getDeptDropDown()


        binding.filtersave.setOnClickListener{

            nAlertDialog.dismiss()
        }
        binding.filtercancel.setOnClickListener{
            nAlertDialog.dismiss()
        }
    }

    private fun sendVerificationMail() {
        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showAlert()
                    Handler(Looper.getMainLooper()).postDelayed({
                        checkVerification()
                    }, 2000)
                }
            }
    }

    private fun checkVerification() {
        val auth = FirebaseAuth.getInstance().currentUser

        //Toast.makeText()

        auth?.reload()?.addOnCompleteListener {
            if (!auth.isEmailVerified) {
                Handler(Looper.getMainLooper()).postDelayed({
                    checkVerification()
                }, 2000)
            }
            else removeVerifiedOption()
        }
    }

    private fun removeVerifiedOption(){
        if(!navView.menu.findItem(R.id.chat).isVisible){
            navView.menu.findItem(R.id.chat).isVisible = true
            navView.menu.findItem(R.id.verify).isVisible = false

            getUniDept()
        }
    }

    private fun getUniDept() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        FirebaseDatabase.getInstance().getReference("Users/$uid/PersonalInfo").get()
            .addOnSuccessListener {
                val uni = it.child("university").value
                val dept = it.child("department").value
                val ref = FirebaseDatabase.getInstance().getReference("University/$uni/Department/$dept")
                var no = 0
                ref.child("userNo").get().addOnSuccessListener { ds ->
                    no = ds.value.toString().toInt()
                    ref.child("UID").child("$no").setValue(uid).addOnSuccessListener {
                        val value = mapOf(
                            "userNo" to (no + 1).toString(),
                        )
                        ref.updateChildren(value).addOnSuccessListener {
                        }
                    }
                    //Log.d("hsfbn :::::::::", it.child("university").value.toString())
                }
            }
    }

    private fun showAlert(){
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setTitle("Verification In Progress")
        alertDialogBuilder.setMessage("Verification mail has been sent. Please DO NOT close this app. Minimize if required.")
        alertDialogBuilder.setPositiveButton("OK") { _, _ -> }
        alertDialogBuilder.create()?.show()
    }

    private fun changeAccountMenuItems(flag : Boolean){
        val menu : Menu = navView.menu
        menu.findItem(R.id.signin).isVisible =!flag
        menu.findItem(R.id.signup).isVisible =!flag
        menu.findItem(R.id.signout).isVisible =flag

        val user = FirebaseAuth.getInstance().currentUser

        val uid = user?.uid
        FirebaseDatabase.getInstance().getReference("/Users/$uid").get().addOnSuccessListener {
            val admin = it.child("admin").value == "1"

            menu.findItem(R.id.adminpanel).isVisible = (it.exists() && admin)
            menu.findItem(R.id.profile).isVisible = (it.exists() && !admin)
            menu.findItem(R.id.verify).isVisible = (it.exists() && !admin && user?.isEmailVerified == false)
            menu.findItem(R.id.chat).isVisible = (it.exists() && !admin && user?.isEmailVerified == true)

            postBtn.isVisible = (it.exists() && admin)

        }
    }

    private fun loadMenuItemActivity(itemNo : Int){
        val intent = Intent(this, MenuItemsActivity::class.java)
        intent.putExtra("ItemNo", itemNo)
        startActivity(intent)
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        //transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
        transaction.replace(R.id.main_frame, fragment)
        transaction.commit()
    }

    /*private fun setMultiFuncBtn(){
        val button = binding.homeMultiFunBtn
        when(val user = FirebaseAuth.getInstance().currentUser){
            null ->{
                button.text = getString(R.string.sign_in)
                button.setOnClickListener {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace((view?.parent as ViewGroup).id, SignInFragment())
                        ?.addToBackStack(null)
                        ?.commit()
                }
            }
            else -> {
                if(!user.isEmailVerified){
                    button.text = getString(R.string.verify_email)
                    button.setOnClickListener{
                        user.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("TAG ::::::: ", "Email sent.")
                                }
                            }
                    }
                }
                else{
                    button.text = getString(R.string.chat)
                    button.setOnClickListener{
                        //TODO : Implement Chat System
                    }
                }
            }
        }
    }
    */



   /* override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected((item))){
            return true
        }
        return super.onOptionsItemSelected(item)
    }*/

    /*
    //to fetch all the users of firebase Auth app
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    DatabaseReference usersdRef = rootRef.child("users");

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                String name = ds.child("name").getValue(String.class);

                        Log.d("TAG", name);

                        array.add(name);

            }
            ArrayAdapter<String> adapter = new ArrayAdapter(OtherUsersActivity.this, android.R.layout.simple_list_item_1, array);

            mListView.setAdapter(adapter)

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
    usersdRef.addListenerForSingleValueEvent(eventListener);
    */
/*
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
    */

}
