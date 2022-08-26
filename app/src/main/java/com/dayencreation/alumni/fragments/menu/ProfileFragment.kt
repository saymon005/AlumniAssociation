package com.dayencreation.alumni.fragments.menu

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dayencreation.alumni.databinding.EditprofilleBinding
import com.dayencreation.alumni.databinding.FragmentProfileBinding
import com.dayencreation.alumni.fragments.main.PostNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val userUid = arguments?.getString("uid").toString()
        val i = arguments?.getString("req").toString().toInt()
        Log.d("Fggdsfh :::::::  ", userUid)

        loadProfile(userUid, i)

        return binding.root
    }

    private fun loadProfile(_id: String, i : Int) {
        val thisUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        var id = ""
        if (_id == ""){
            id =  thisUid
            binding.friendReq.isVisible = false
            binding.friendReqAccept.isVisible = false
            binding.friendReqReject.isVisible = false
            binding.profileEdit.isVisible = true
        }

        else {
            id = _id
            binding.friendReq.isVisible = (i != 1)
            binding.friendReqAccept.isVisible = (i == 1)
            binding.friendReqReject.isVisible = (i == 1)
            binding.profileEdit.isVisible = false
        }
        var b = ""
        var p = ""
        var f = ""
        var ad = ""

        val ref = FirebaseDatabase.getInstance().getReference("Users/$id")
        ref.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(it: DataSnapshot) {
                binding.profileUserName.text =
                    it.child("PersonalInfo").child("fullName").value.toString()
                binding.profileUniname.text =
                    it.child("PersonalInfo").child("university").value.toString()
                binding.profiledeptname.text =
                    it.child("PersonalInfo").child("department").value.toString()
                binding.profileuniid.text =
                    it.child("PersonalInfo").child("universityId").value.toString()
                binding.profilegradyr.text =
                    it.child("PersonalInfo").child("graduationYear").value.toString()
                binding.profilesessn.text = it.child("PersonalInfo").child("session").value.toString()

                b = (it.child("profileInfo").child("bio").value?:"") as String
                p = (it.child("profileInfo").child("phone").value?:"") as String
                f = (it.child("profileInfo").child("fb").value?:"") as String
                ad = (it.child("profileInfo").child("address").value?:"") as String

                binding.etBio.text = b
                binding.profilephn.text = "Phone: $p"
                binding.profilefblink.text = "Facebook: $f"
                binding.profileaddress.text = "Address: $ad"
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })

        val ref2 = FirebaseDatabase.getInstance().getReference("Users/$thisUid")

        binding.friendReq.setOnClickListener{
            ref2.child("PersonalInfo").child("fullName").get().addOnSuccessListener {
                ref.child("FriendReq").child(thisUid).setValue(it.value.toString())
            }
        }

        binding.friendReqAccept.setOnClickListener{
            ref2.child("Friends").get().addOnSuccessListener {
                val no =  it.child("friendCount").value.toString().toInt()
                    ref.child("uids").child("$no").setValue(id).addOnSuccessListener {
                        val value = mapOf(
                            "friendCount" to (no + 1).toString(),
                        )
                        ref2.child("FriendReq").child(id).removeValue()
                        ref.updateChildren(value).addOnSuccessListener {
                        }
                    }
                }
            }

        binding.friendReqReject.setOnClickListener{
            ref2.child("FriendReq").child(id).removeValue()
        }

        binding.profileEdit.setOnClickListener {
            val binding1 = EditprofilleBinding.inflate(layoutInflater)
            val nBuilder = AlertDialog.Builder(context)
                .setView(binding1.root)
                .setTitle("Enter Info")
            val nAlertDialog = nBuilder.show()

            binding1.dialogbio.setText(b)
            binding1.dialogphone.setText(p)
            binding1.dialogfb.setText(f)
            binding1.dialogaddress.setText(ad)

            binding1.dialogUpdateBtn.setOnClickListener {
                val ref2 = FirebaseDatabase.getInstance().getReference("Users/$id/profileInfo")
                ref2.get().addOnSuccessListener {
                    if (it.exists()) {
                        val user = mapOf(
                            "bio" to binding1.dialogbio.text.toString(),
                            "phone" to binding1.dialogphone.text.toString(),
                            "fb" to binding1.dialogfb.text.toString(),
                            "address" to binding1.dialogaddress.text.toString()
                        )
                        ref2.updateChildren(user).addOnSuccessListener {
                            nAlertDialog.dismiss()
                        }.addOnFailureListener {

                        }
                    } else {
                        ref2.setValue(
                            ProfileInfo(
                                binding1.dialogbio.text.toString(),
                                binding1.dialogphone.text.toString(),
                                binding1.dialogfb.text.toString(),
                                binding1.dialogaddress.text.toString()
                            )
                        )
                    }
                }
                binding1.dialogCancelBtn.setOnClickListener {
                    nAlertDialog.dismiss()
                }
            }
        }
    }
}

data class ProfileInfo(
    var bio: String? = null,
    var phone: String? =null,
    var fb: String?=null,
    var address: String?=null
)