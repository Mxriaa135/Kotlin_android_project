package com.example.apppostagens.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.example.apppostagens.Activity.EditProfileActivity
import com.example.apppostagens.Activity.LoginActivity
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.example.apppostagens.Utils.UserFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private lateinit var authenticator : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var username : TextView
    private lateinit var name : TextView
    private lateinit var editButton : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)
        initializeComponents(view)
        loadData()

        //toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar_profile)
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

        editButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, EditProfileActivity::class.java)
                startActivity(intent)
            }
        })

        return view
    }
    private fun signOut(){
        authenticator = FirebaseConfiguration.getFirebaseAuthReference()

        try {
            authenticator.signOut()
        } catch (e :Exception){
            e.printStackTrace()
        }
    }

    private fun loadData(){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot : DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val user =  dataSnapshot.getValue(User::class.java)
                    user?.let {
                        username.text = it.getUsername()
                        name.text = it.getName()

                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    fun initializeComponents(view: View){
        val currentUser = UserFirebase.getCurrentUser()
        if (currentUser != null) {
            val userRef = currentUser.uid
            databaseReference = FirebaseConfiguration.getFirebaseReference().child("User").child(userRef)
        } else {
            return
        }
        username = view.findViewById(R.id.usernameProfile)
        name = view.findViewById(R.id.textNameProfile)
        editButton = view.findViewById(R.id.editButtonProfile)
    }

}