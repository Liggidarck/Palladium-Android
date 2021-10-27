package com.george.vector.develop

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.george.vector.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class DevelopKotlinActivity : AppCompatActivity() {

    val TAG = "DEVELOPER ACTIVITY"

//    private val easyPermissionManager = EasyPermissionManager(this)

    private lateinit var galleryBtn: Button
    private lateinit var cameraBtn: Button
    private lateinit var sendToServer: Button

    lateinit var NAME_IMAGE: String

    var storage_reference: StorageReference? = null
    var firebase_storage: FirebaseStorage? = null

    private val imageDev: ImageView by lazy {
        findViewById(R.id.image_dev)
    }

    private var tempImageUri: Uri? = null

    private val selectPictureLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        //NOT NULL ПРОВЕРКА!
        Log.d("DEVELOPER ACTIVITY", "Image uri: $it");
        tempImageUri = it
        imageDev.setImageURI(tempImageUri)
    }

    private var tempImageFilePath = ""
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { succes ->

        if (succes) {
            imageDev.setImageURI(tempImageUri)
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_develop_kotlin)

        galleryBtn = findViewById(R.id.gallery_btn)
        cameraBtn = findViewById(R.id.camera_btn)
        sendToServer = findViewById(R.id.send_to_server)

        firebase_storage = FirebaseStorage.getInstance()
        storage_reference = firebase_storage!!.getReference()

        galleryBtn.setOnClickListener {

//            easyPermissionManager.requestPermission(
//                "permission",
//                "permission are necessary",
//                "setting",
//                arrayOf(
//                    android.Manifest.permission.CAMERA,
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                )
//            ) {
//                selectPictureLauncher.launch("image/*")
//            }
        }


        cameraBtn.setOnClickListener {

//            easyPermissionManager.requestPermission(
//                "permission",
//                "permission are necessary",
//                "setting",
//                arrayOf(
//                    android.Manifest.permission.CAMERA,
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                )
//            ) {
//                tempImageUri = FileProvider.getUriForFile(this,
//                    "com.george.vector.provider",
//                    createImageFile().also {
//                        tempImageFilePath = it.absolutePath
//                    })
//
//                cameraLauncher.launch(tempImageUri)
//            }

        }

        sendToServer.setOnClickListener {
            Log.d(TAG, "Uri: $tempImageUri");
            uploadImage(tempImageUri)
        }


    }

    private fun uploadImage(filePath: Uri? = null) {
        if (filePath != null) {
            var bmp: Bitmap? = null
            try {
                bmp = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val byteArrayOutputStream = ByteArrayOutputStream()
            bmp!!.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
            val data = byteArrayOutputStream.toByteArray()

            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Загрузка...")
            progressDialog.show()
            NAME_IMAGE = UUID.randomUUID().toString()

            val ref: StorageReference = storage_reference!!.child("images/$NAME_IMAGE")
            ref.putBytes(data)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                }
                .addOnProgressListener {
                        taskSnapshot: UploadTask.TaskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    progressDialog.setMessage(String.format("Прогресс: %d%%", progress.toInt()))
                }

        } else {

            NAME_IMAGE = null.toString()
            onBackPressed()

        }
    }

    private fun  createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return  File.createTempFile("temp_image", ".jpg", storageDir);

    }

}