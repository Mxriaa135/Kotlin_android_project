package com.example.apppostagens.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.apppostagens.Activity.ViewPostActivity
import com.example.apppostagens.Adapter.GridAdapter
import com.example.apppostagens.Model.Post
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.example.apppostagens.Utils.UserFirebase
import com.example.apppostagens.databinding.FragmentProfileFriendBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FriendProfileFragment : Fragment() {

    private var _binding: FragmentProfileFriendBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseRef: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private lateinit var currentUserRef : DatabaseReference
    private lateinit var friendUserRef: DatabaseReference
    private lateinit var followersRef: DatabaseReference
    private lateinit var postUserRef: DatabaseReference
    private lateinit var eventListener: ValueEventListener

    private var currentUser: User? = null
    private var userData : User? = null
    private var idFriendUser : String? = null
    private lateinit var idCurrentUser : String

    private lateinit var gridView: GridView
    private lateinit var adapter: GridAdapter
    private var listPosts: MutableList<Post> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileFriendBinding.inflate(inflater, container, false)
        initializeComponents()

        //toolbar
        val toolbar = binding.include.toolbarProfile
        toolbar.setTitle("")
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)


        binding.include.arrowBackImage.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        loadPostsUser()

        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val post: Post = listPosts.get(position)
                openPostView(post)
            }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadDataUser()
        loadDataCurrentUser()
    }

    private fun loadDataCurrentUser(){
        currentUserRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                checkIfFollowsFriend()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun loadDataUser(){
        eventListener = friendUserRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot : DataSnapshot) {
                userData =  dataSnapshot.getValue(User::class.java)
                userData?.let {
                    if (!isAdded || view == null) return

                    binding.include.usernameProfile.text = it.getUsername()
                    binding.textNameProfile.text = it.getName()
                    binding.textNumPost.text = it.getPosts().toString()
                    binding.textNumFollowers.text = it.getFollowers().toString()
                    binding.textNumFollowing.text = it.getFollowing().toString()

                    it.let{
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
        postUserRef.addListenerForSingleValueEvent(object : ValueEventListener{
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

    private fun openPostView(post: Post) {
        val intent = Intent(requireContext(), ViewPostActivity::class.java)
        intent.putExtra("userId", post.getUserId())
        intent.putExtra("postId", post.getId())
        startActivity(intent)
    }

    private fun checkIfFollowsFriend(){
        eventListener = followersRef.child(idFriendUser!!).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(idCurrentUser)){
                    binding.followButtonProfile.text = "Seguindo"
                    binding.followButtonProfile.setOnClickListener {
                        removeFollower(currentUser!!, userData!!)
                    }
                }
                else{
                    binding.followButtonProfile.text = "Seguir"
                    binding.followButtonProfile.setOnClickListener {
                        saveFollower(currentUser!!, userData!!)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun saveFollower(currentUser : User, friendUser : User){
        followersRef
            .child(friendUser.getId())
            .child(currentUser.getId())
            .setValue(true)
        binding.followButtonProfile.text = "Seguindo"

        currentUser.setFollowing(currentUser.getFollowing() + 1)
        friendUser.setFollowers(friendUser.getFollowers()+1)
        friendUser.update()
        currentUser.update()
    }

    private fun removeFollower(currentUser : User, friendUser : User){
        followersRef
            .child(friendUser.getId())
            .child(currentUser.getId())
            .removeValue()
        binding.followButtonProfile.text = "Seguir"

        currentUser.setFollowing(currentUser.getFollowing() - 1)
        friendUser.setFollowers(friendUser.getFollowers()-1)
        friendUser.update()
        currentUser.update()
    }

    private fun initializeComponents(){
        idFriendUser = arguments?.getString("userId")
        idCurrentUser = UserFirebase.getCurrentUser()!!.uid
        firebaseRef = FirebaseConfiguration.getFirebaseReference()
        userRef = firebaseRef.child("User")
        currentUserRef = userRef.child(idCurrentUser)
        friendUserRef = userRef.child(idFriendUser!!)
        followersRef = firebaseRef.child("Followers")
        postUserRef = firebaseRef.child("Post").child(idFriendUser.toString())
        gridView = binding.gridView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        friendUserRef.removeEventListener(eventListener)
    }

}