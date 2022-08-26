package com.dayencreation.alumni.fragments.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dayencreation.alumni.adapters.MyRecyclerViewAdapter
import com.dayencreation.alumni.databinding.FragmentAdminPanelBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminPanelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminPanelFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentAdminPanelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminPanelBinding.inflate(layoutInflater)
        fetchUsers()
        return binding.root
    }

    private fun fetchUsers(){
        val lists = ArrayList<ArrayList<String>>()

        FirebaseDatabase.getInstance().reference.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lists.clear()
                snapshot.children.forEach { ds ->
                    if(ds.child("RegistrationInfo").child("valid").value == "1")  return@forEach
                    Log.d("User :::: ", ds.toString())
                    val list = arrayListOf(
                        ds.key.toString(),
                        ds.child("PersonalInfo").child("fullName").value.toString(),
                        ds.child("PersonalInfo").child("university").value.toString(),
                        ds.child("PersonalInfo").child("department").value.toString(),
                        ds.child("PersonalInfo").child("universityId").value.toString(),
                        ds.child("PersonalInfo").child("graduationYear").value.toString(),
                        ds.child("PersonalInfo").child("session").value.toString()
                    )
                    lists.add(list)
                }
                binding.rvUserCounter.text = lists.count().toString()
                binding.rvRequests.adapter = MyRecyclerViewAdapter(lists)
                binding.rvRequests.layoutManager = LinearLayoutManager(context)
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
        /*FirebaseDatabase.getInstance().reference.child("Users").get().addOnSuccessListener {
            Log.d("Fetch", "fetching....")
            it.children.forEach { ds ->
                if(ds.child("RegistrationInfo").child("valid").value == "1")  return@forEach
                Log.d("User :::: ", ds.toString())
                val list = arrayListOf(
                    ds.key.toString(),
                    ds.child("PersonalInfo").child("fullName").value.toString(),
                    ds.child("PersonalInfo").child("university").value.toString(),
                    ds.child("PersonalInfo").child("department").value.toString(),
                    ds.child("PersonalInfo").child("universityId").value.toString(),
                    ds.child("PersonalInfo").child("graduationYear").value.toString(),
                    ds.child("PersonalInfo").child("session").value.toString()
                )
                lists.add(list)
            }
            binding.rvUserCounter.text = lists.count().toString()
            Log.d("Fetch", "fetching done")
            //binding.rvRequests.adapter = MyRecyclerViewAdapter(lists)
            //binding.rvRequests.layoutManager = LinearLayoutManager(context)
        }
        //adapter.setClickListener(this)*/
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdminPanelFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminPanelFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}