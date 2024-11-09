package it.polito.mad.carpooling

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private lateinit var editText: EditText
    private lateinit var editText2: EditText
    private lateinit var editText3: EditText
    private lateinit var editText4: EditText
    private lateinit var imageViewEdit : ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: ListViewModel by activityViewModels()
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private lateinit var nameDrawer: TextView

    private val CAMERA_REQUEST_CODE = 1
    private val IMAGE_CHOOSE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val fab= activity?.findViewById<FloatingActionButton>(R.id.fab)
        fab?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference


        editText = view.findViewById<EditText>(R.id.editText_edit_profile)
        editText2 = view.findViewById<EditText>(R.id.editText2_edit_profile)
        editText3 = view.findViewById<EditText>(R.id.editText3_edit_profile)
        editText4 = view.findViewById<EditText>(R.id.editText4_edit_profile)
        imageViewEdit = view.findViewById<ImageView>(R.id.imageViewEdit_edit_profile)


        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document("user1")
            .get()
            .addOnSuccessListener { value ->
                if (value != null) {
                    editText.setText(value["fullName"].toString())
                    editText2.setText(value["nickname"].toString())
                    editText3.setText(value["email"].toString())
                    editText4.setText(value["location"].toString())

                    val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.reference
                    val imageRef = storageRef.child(value["image"] as String)
                    val ONE_MEGABYTE: Long = 1024 * 1024
                    var bitmapCar: Bitmap
                    val carRef = storageRef.child("images/tony.jpg")
                    carRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                        bitmapCar = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        imageRef.getBytes(ONE_MEGABYTE)
                            .addOnSuccessListener { bytes ->
                                imageViewEdit.setImageBitmap(
                                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                )
                            }
                            .addOnCanceledListener {
                                imageViewEdit.setImageBitmap(bitmapCar)
                            }
                    }
                }
            }

        val cameraButton = view.findViewById<ImageButton>(R.id.cameraButton)
        // Long press on the ImageButton is needed, not a short click
        registerForContextMenu(cameraButton)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.save -> {

                val db = FirebaseFirestore.getInstance()
                db.collection("users").document("user1")
                    .set(hashMapOf(
                        "fullName" to editText.text.toString(),
                        "nickname" to editText2.text.toString(),
                        "email" to editText3.text.toString(),
                        "location" to editText4.text.toString(),
                        "image" to "images/users/user1"
                        //"image" to imageViewCard.drawable.toBitmap()
                    ))
                findNavController().navigate(R.id.action_editProfileFragment_to_showProfileFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = activity?.menuInflater!!
        inflater.inflate(R.menu.menu_change_image, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.gallery -> {
                chooseImageGallery()
                true
            }
            R.id.use_camera -> {
                dispatchTakePictureIntent()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureProfileIntent ->
            takePictureProfileIntent.resolveActivity(activity?.packageManager!!)?.also {
                startActivityForResult(takePictureProfileIntent, CAMERA_REQUEST_CODE)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageViewEdit.setImageBitmap(imageBitmap)

            // Store the image
            val imageRef = storageReference.child("images/users/user1")
            imageViewEdit.isDrawingCacheEnabled = true
            imageViewEdit.buildDrawingCache()
            val bitmap = (imageViewEdit.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            imageRef.putBytes(data)
        }

        ///findNavController().g

        if (requestCode == IMAGE_CHOOSE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri = data?.data
            imageViewEdit.setImageURI(imageUri)

        }
    }

    private fun chooseImageGallery() {

        val gallery = Intent(Intent.ACTION_PICK)
        gallery.type = "image/*"
        //Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        gallery.setAction(Intent.ACTION_GET_CONTENT)
        //startActivityForResult(gallery, IMAGE_CHOOSE)
        startActivityForResult(Intent.createChooser(gallery, "select picture"), IMAGE_CHOOSE)
    }


    /*
       override fun onSaveInstanceState(outState: Bundle) {
           super.onSaveInstanceState(outState)
           outState.putParcelable("im",imageViewEdit.drawToBitmap())
           outState.putString("ed1",editText.text.toString())
           outState.putString("ed2",editText2.text.toString())
           outState.putString("ed3",editText3.text.toString())
           outState.putString("ed4",editText4.text.toString())
       }


       override fun onViewStateRestored(savedInstanceState: Bundle?) {
           super.onViewStateRestored(savedInstanceState)
           imageViewEdit.setImageBitmap(savedInstanceState?.getParcelable("im"))
           editText.setText(savedInstanceState?.getString("ed1"))
           editText2.setText(savedInstanceState?.getString("ed2"))
           editText3.setText(savedInstanceState?.getString("ed3"))
           editText4.setText(savedInstanceState?.getString("ed4"))
       }

        */

}