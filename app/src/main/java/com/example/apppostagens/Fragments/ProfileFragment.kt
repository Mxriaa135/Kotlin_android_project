package com.example.apppostagens.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.apppostagens.Activity.EditProfileActivity
import com.example.apppostagens.Activity.LoginActivity
import com.example.apppostagens.Activity.ViewPostActivity
import com.example.apppostagens.Adapter.GridAdapter
import com.example.apppostagens.Model.Post
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.example.apppostagens.Utils.UserFirebase
import com.example.apppostagens.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var authenticator : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userReference: DatabaseReference
    private lateinit var postReference: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener
    private lateinit var currentUserId: String

    private lateinit var gridView: GridView
    private lateinit var adapter: GridAdapter
    private var listPosts: MutableList<Post> = mutableListOf()
    private lateinit var numPosts : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        initializeComponents()
        loadData()

        //toolbar
        val toolbar = binding.include.toolbarProfile
        toolbar.setTitle("")
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_profile, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_sair -> {
                        signOut()
                        startActivity(Intent(context, LoginActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)

        binding.editButtonProfile.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivity(intent)
        }

        loadPostsUser()

        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val post: Post = listPosts.get(position)
                openPostView(post)
            }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::valueEventListener.isInitialized) {
            databaseReference.removeEventListener(valueEventListener)
        }
    }

    private fun signOut(){
        authenticator = FirebaseConfiguration.getFirebaseAuthReference()
        try {
            authenticator.signOut()
        } catch (e :Exception){
            e.printStackTrace()
        }
    }

    private fun openPostView(post: Post) {
        val intent = Intent(requireContext(),ViewPostActivity::class.java)
        intent.putExtra("userId", currentUserId)
        intent.putExtra("postId", post.getId())
        startActivity(intent)
    }

    private fun loadData(){
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                user?.let {
                    if (!isAdded || view == null) return

                    binding.include.usernameProfile.text = it.getUsername()
                    binding.textNameProfile.text = it.getName()
                    binding.textNumPost.text = it.getPosts().toString()
                    binding.textNumFollowers.text = it.getFollowers().toString()
                    binding.textNumFollowing.text = it.getFollowing().toString()

                    it.let {
                        Glide.with(requireContext())
                            .load(it.getUserImage())
                            .placeholder(R.drawable.profile)
                            .error(R.drawable.profile)
                            .into(binding.userImage)

                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    private fun loadPostsUser(){
        postReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val sizeGrid = resources.displayMetrics.widthPixels
                val widthImage = sizeGrid / 3
                gridView.columnWidth = widthImage

                val list: MutableList<String> = mutableListOf()
                for(ds in dataSnapshot.children){
                    val post : Post? = ds.getValue(Post::class.java)
                    listPosts.add(post!!)
                    list.add(post.getImageUrl())
                }

                adapter = GridAdapter(requireContext(), R.layout.grid_post, list)
                gridView.setAdapter(adapter)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun initializeComponents(){
        currentUserId = UserFirebase.getCurrentUser()!!.uid
        databaseReference = FirebaseConfiguration.getFirebaseReference()
        userReference = databaseReference.child("User").child(currentUserId)
        postReference = databaseReference.child("Post").child(currentUserId)
        gridView = binding.gridView

    }

}