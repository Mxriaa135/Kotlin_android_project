package com.example.apppostagens.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.apppostagens.Activity.AddActivity
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.example.apppostagens.databinding.FragmentAddBinding
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddFragment : Fragment() {

    private lateinit var storageRef: StorageReference
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageOrigin : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        initializeComponents()

        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    when (imageOrigin) {
                        "camera" -> {
                            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                            val i = Intent(context, AddActivity::class.java)
                            i.putExtra("imageBitmap", imageBitmap)
                            startActivity(i)
                        }

                        "gallery" -> {
                            val imageUri = result.data?.data
                            val i = Intent(context, AddActivity::class.java)
                            i.putExtra("imageUri", imageUri.toString())
                            startActivity(i)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        binding.cameraButton.setOnClickListener{
            imageOrigin = "camera"
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            getResult.launch(intent)
        }

        binding.galleryButton.setOnClickListener{
            imageOrigin = "gallery"
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getResult.launch(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeComponents(){
        storageRef = FirebaseConfiguration.getFirebaseStorage()
    }

}