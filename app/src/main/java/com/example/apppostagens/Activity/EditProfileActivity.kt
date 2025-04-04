package com.example.apppostagens.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.example.apppostagens.Utils.UserFirebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.IOException

class EditProfileActivity : AppCompatActivity() {

    private lateinit var databaseReference : DatabaseReference
    private lateinit var dataCurrentUser : User
    private lateinit var buttonBack : ImageButton
    private lateinit var editTextUsername : EditText
    private lateinit var editTextName : EditText
    private lateinit var buttonSave : Button
    private lateinit var imageUser : ImageView
    private lateinit var textEditPhoto : TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var storageRef : StorageReference
    private lateinit var idUser : String
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.navigationBarColor = ContextCompat.getColor(this, R.color.Transparent)
        initializeComponents()
        loadData()

        buttonBack.setOnClickListener{
            finish()
        }

        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                    try {
                        progressBar.visibility = View.VISIBLE
                        imageUser.visibility = View.INVISIBLE

                        uri?.let {
                            //Orientação da foto
                            val inputStream = contentResolver.openInputStream(it)
                            val exif = androidx.exifinterface.media.ExifInterface(inputStream!!)
                            val orientation = exif.getAttributeInt(
                                androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
                                androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
                            )

                            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                            val matrix = android.graphics.Matrix()

                            when (orientation) {
                                androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                                androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                                androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                            }

                            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                            imageUser.setImageBitmap(rotatedBitmap)

                            val baos = ByteArrayOutputStream()
                            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
                            val dataImage = baos.toByteArray()

                            val imageRef = storageRef
                                .child("ImageUsers")
                                .child("$idUser.jpeg")

                            val uploadTask: UploadTask = imageRef.putBytes(dataImage)
                            uploadTask.addOnFailureListener {
                                Toast.makeText(this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show()
                            }.addOnSuccessListener { _ ->
                                imageRef.downloadUrl.addOnSuccessListener { uri ->
                                    this.uri = uri
                                    progressBar.visibility = View.GONE
                                    imageUser.visibility = View.VISIBLE
                                    Toast.makeText(this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
            }
        }

        imageUser.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getResult.launch(intent)
        }
        textEditPhoto.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getResult.launch(intent)
        }

        buttonSave.setOnClickListener {
            updateData()
        }
    }

    private fun updateData() {
        val newUserImage = uri.toString()
        val newUsername = editTextUsername.text.toString()
        val newName = editTextName.text.toString()

        if (newUserImage != dataCurrentUser.getUserImage() ||
            newUsername != dataCurrentUser.getUsername() ||
            newName != dataCurrentUser.getName()){

            UserFirebase.updateImageUser(uri)
            dataCurrentUser.setUserImage(newUserImage)
            UserFirebase.updateNameUser(newUsername)
            dataCurrentUser.setUsername(newUsername)
            dataCurrentUser.setName(newName)
            dataCurrentUser.update()
            Toast.makeText(this, "Dados Atualizados", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Altere algo para salvar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadData(){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot : DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    if (isFinishing || isDestroyed) return
                    val user =  dataSnapshot.getValue(User::class.java)
                    user?.let {
                        dataCurrentUser = it
                        editTextUsername.setText(it.getUsername())
                        editTextName.setText(it.getName())

                        if(it.getUserImage() != ""){
                            Glide.with(this@EditProfileActivity)
                                .load(it.getUserImage())
                                .placeholder(R.drawable.profile)
                                .error(R.drawable.profile)
                                .into(imageUser)
                        }
                        }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    private fun initializeComponents(){
        val currentUser = UserFirebase.getCurrentUser()
        currentUser?.let {
            val userRef = currentUser.uid
            databaseReference =
                FirebaseConfiguration.getFirebaseReference().child("User").child(userRef)
        }
        dataCurrentUser = UserFirebase.getDataCurrentUser()!!
        storageRef = FirebaseConfiguration.getFirebaseStorage()
        idUser = UserFirebase.getCurrentUser()?.uid.toString()
        uri = dataCurrentUser.getUserImage().toUri()

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextName = findViewById(R.id.editTextName)
        buttonSave = findViewById(R.id.buttonSaveEdit)
        buttonBack = findViewById(R.id.buttonBackEdit)
        imageUser = findViewById(R.id.userImage)
        textEditPhoto = findViewById(R.id.textEditPhoto)
        progressBar = findViewById(R.id.progressBarEdit)
    }
}