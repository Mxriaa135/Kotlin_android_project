package com.example.apppostagens.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apppostagens.Adapter.PostAdapter
import com.example.apppostagens.Adapter.SearchAdapter
import com.example.apppostagens.Model.Post
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.example.apppostagens.Utils.UserFirebase
import com.example.apppostagens.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment(){

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var listUsers: MutableList<User> = mutableListOf()
    private lateinit var databaseRef : DatabaseReference
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                searchUsers(newText?.lowercase())
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchUsers(query)
                return false
            }
        })
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.setLayoutManager(LinearLayoutManager(context))

        adapter = SearchAdapter(listUsers, object : SearchAdapter.OnUserClickListener{
            override fun onUserClick(userId: String) {
                openFriendProfile(userId)
            }
        })
        binding.recyclerView.adapter = adapter

        return binding.root
    }


    private fun searchUsers(text : String?){
        listUsers.clear()
        databaseRef = FirebaseConfiguration.getFirebaseReference().child("User")

        val query : Query = databaseRef.orderByChild("username")
            .startAt(text)
            .endAt(text + "\uf8ff")

        if(text != null && text.length >= 1){
            listUsers.clear()
            query.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listUsers.clear()
                    for (snapshot in dataSnapshot.children) {
                        val user = snapshot.getValue(User::class.java)
                        if (user!!.getUsername().startsWith(text) && user.getId() != UserFirebase.getCurrentUser()!!.uid) {
                            listUsers.add(user)
                            adapter.notifyDataSetChanged()
                        }
                    }
                    if(listUsers.isEmpty()){
                        listUsers.clear()
                        adapter.notifyDataSetChanged()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        else{
            listUsers.clear()
            adapter.notifyDataSetChanged()
        }
    }

    private fun openFriendProfile(userId: String) {
        val fragment = FriendProfileFragment()
        val bundle = Bundle()
        bundle.putString("userId", userId)
        fragment.arguments = bundle
        findNavController().navigate(R.id.FriendProfileFragment, bundle)
    }
}