package it.polito.mad.carpooling

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private lateinit var editText: EditText
    private lateinit var editText2: EditText
    private lateinit var editText3: EditText
    private lateinit var editText4: EditText
    private lateinit var imageViewEdit : ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: ListViewModel by activityViewModels()

    private lateinit var nameDrawer: TextView

    private val CAMERA_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val fab= activity?.findViewById<FloatingActionButton>(R.id.fab)
        fab?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = view.findViewById<EditText>(R.id.editText_edit_profile)
        editText2 = view.findViewById<EditText>(R.id.editText2_edit_profile)
        editText3 = view.findViewById<EditText>(R.id.editText3_edit_profile)
        editText4 = view.findViewById<EditText>(R.id.editText4_edit_profile)
        imageViewEdit = view.findViewById<ImageView>(R.id.imageViewEdit_edit_profile)


        sharedPreferences = activity?.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!

        if(arguments!=null) {
            imageViewEdit.setImageBitmap(arguments?.getParcelable("group22.lab1.Image_Profile"))
            editText.setText(arguments?.getString("group22.lab1.full_name"))
            editText2.setText(arguments?.getString("group22.lab1.nickname"))
            editText3.setText(arguments?.getString("group22.lab1.email"))
            editText4.setText(arguments?.getString("group22.lab1.location"))
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
                //nameDrawer.text = editText.text.toString()
                with (sharedPreferences.edit()) {
                    putString("FULL_NAME", editText.text.toString())
                    putString("NICK_NAME", editText2.text.toString())
                    putString("EMAIL_ADD", editText3.text.toString())
                    putString("USER_LOCA", editText4.text.toString())
                    apply()
                }
                val bundle = Bundle()
                bundle.putParcelable("group22.lab1.Image_Profile",imageViewEdit.drawable.toBitmap())
                bundle.putString("group22.lab1.full_name", editText.text.toString())
                bundle.putString("group22.lab1.nickname", editText2.text.toString())
                bundle.putString("group22.lab1.email", editText3.text.toString())
                bundle.putString("group22.lab1.location", editText4.text.toString())
                findNavController().navigate(R.id.action_editProfileFragment_to_showProfileFragment,bundle)
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
            //viewModel.change_image_drawer(imageBitmap)
        }
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