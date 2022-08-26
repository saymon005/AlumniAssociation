package com.dayencreation.alumni

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dayencreation.alumni.fragments.authentication.RegistrationFragment
import com.dayencreation.alumni.fragments.authentication.SignInFragment
import com.dayencreation.alumni.fragments.menu.AdminPanelFragment
import com.dayencreation.alumni.fragments.menu.ChatFragment
import com.dayencreation.alumni.fragments.menu.ProfileFragment
import com.dayencreation.alumni.fragments.menu.SettingsFragment

class MenuItemsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_items)
        when(intent.getIntExtra("ItemNo", 0)){
            0 -> gotoProfile(ProfileFragment())
            1 -> replaceFragment(SettingsFragment())
            2 -> replaceFragment(SignInFragment())
            3 -> replaceFragment(RegistrationFragment())
            4 -> replaceFragment(AdminPanelFragment())
            5 -> replaceFragment(ChatFragment())
        }
    }

    private fun gotoProfile(fragment: Fragment){
        val bundle = Bundle()
        bundle.putString("uid", "")
        bundle.putString("req", "0")
        fragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        //transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
        transaction.replace(R.id.item_frame, fragment)
        transaction.commit()
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        //transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
        transaction.replace(R.id.item_frame, fragment)
        transaction.commit()
    }
}