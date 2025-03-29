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
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.bumptech.glide.manager.Lifecycle
import com.example.apppostagens.Activity.LoginActivity
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var authenticator : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)

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

}