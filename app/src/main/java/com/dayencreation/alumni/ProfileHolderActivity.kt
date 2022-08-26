package com.dayencreation.alumni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dayencreation.alumni.fragments.main.FriendReqFragment
import com.dayencreation.alumni.fragments.menu.ProfileFragment

class ProfileHolderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_holder)

        val uid = intent.getStringExtra("uid").toString()
        val req = intent.getStringExtra("req").toString()

        gotoProfile(ProfileFragment(), uid, req)
    }

    private fun gotoProfile(fragment: Fragment, uid : String, req : String){
        val bundle = Bundle()
        bundle.putString("uid", uid)
        bundle.putString("req", req)
        fragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        //transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
        transaction.replace(R.id.profile_frame, fragment)
        transaction.commit()
    }
}