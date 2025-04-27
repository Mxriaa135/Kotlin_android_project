package com.example.apppostagens.Activity

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.apppostagens.Model.Post
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.example.apppostagens.Utils.UserFirebase
import com.example.apppostagens.databinding.ActivityAddBinding
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddActivity : AppCompatActivity() {
    private lateinit var storageRef : StorageReference
    private lateinit var binding: ActivityAddBinding
    private lateinit var dataImage: ByteArray
    private lateinit var post : Post
    private lateinit var idUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initializeComponents()
        handleIntentImage()

        binding.toolbarAdd.textAdd.setOnClickListener {
            createPost()
        }

        binding.toolbarAdd.arrowBackImage.setOnClickListener{
            finish()
        }
    }

    private fun imageOrientation(uri : Uri) : Bitmap{
        val inputStream = contentResolver.openInputStream(uri)
        val exif = androidx.exifinterface.media.ExifInterface(inputStream!!)
        val orientation = exif.getAttributeInt(
            androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
            androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
        )

        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val matrix = android.graphics.Matrix()

        when (orientation) {
            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(
                90f
            )

            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(
                180f
            )

            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(
                270f
            )
        }

        val rotatedBitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )

        return rotatedBitmap
    }

    private fun handleIntentImage() {
        intent.getStringExtra("imageUri")?.let {
            val uri = Uri.parse(it)
            val rotatedBitmap = imageOrientation(uri)
            binding.imagePost.setImageBitmap(rotatedBitmap)
            val resizedBitmap = Bitmap.createScaledBitmap(
                rotatedBitmap,
                binding.imagePost.width,
                binding.imagePost.height,
                true
            )
            val baos = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            dataImage = baos.toByteArray()
        }

        intent.getParcelableExtra<Bitmap>("imageBitmap")?.let {
            val imageBitmap = it
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            dataImage = baos.toByteArray()
            binding.imagePost.setImageBitmap(it)
        }
    }

    private fun createPost(){
        binding.progressBar.visibility = View.VISIBLE
        post.setDescription(binding.editTextDescription.text.toString())
        post.setDate(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().time))
        post.setUserId(idUser)
        post.add()

        val imageRef = storageRef
            .child("ImagePosts")
            .child("${post.getId()}.jpeg")

        val uploadTask: UploadTask = imageRef.putBytes(dataImage)
        uploadTask.addOnFailureListener {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(
                this,
                "Erro ao fazer upload da imagem",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnSuccessListener { _ ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                post.setImageUrl(uri.toString())
                if(post.save()){
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Sucesso ao fazer upload da imagem",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }

    private fun initializeComponents(){
        storageRef = FirebaseConfiguration.getFirebaseStorage()
        post = Post()
        idUser = UserFirebase.getDataCurrentUser()!!.getId()
    }
}