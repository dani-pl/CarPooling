package it.polito.mad.carpooling


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.core.view.drawToBitmap
import java.io.File


class EditProfileActivity : AppCompatActivity() {
    // Declaration of properties

    private lateinit var editText: EditText
    private lateinit var editText2: EditText
    private lateinit var editText3: EditText
    private lateinit var editText4: EditText
    private lateinit var imageViewEdit : ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private val CAMERA_REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialization of properties
        editText =findViewById<EditText>(R.id.editText)
        editText2 =findViewById<EditText>(R.id.editText2)
        editText3 =findViewById<EditText>(R.id.editText3)
        editText4 =findViewById<EditText>(R.id.editText4)
        imageViewEdit = findViewById<ImageView>(R.id.imageViewEdit)

        sharedPreferences = getSharedPreferences("SHARED_PREF",Context.MODE_PRIVATE)
        imageViewEdit.setImageBitmap(intent.getParcelableExtra("group22.lab1.Image_Profile"))
        
        //Here we retrieve the final values of the variables on ShowProfileActivity
        val full_name_init: String? = intent.getStringExtra("group22.lab1.FULL_NAME")
        editText.setText(full_name_init)
        val nick_name_init: String? = intent.getStringExtra("group22.lab1.Nickname")
        editText2.setText(nick_name_init)
        val email_add_init: String? = intent.getStringExtra("group22.lab1.email")
        editText3.setText(email_add_init)
        val user_loca_init: String? = intent.getStringExtra("group22.lab1.Location")
        editText4.setText(user_loca_init)


        val cameraButton = findViewById<ImageButton>(R.id.cameraButton)
        // Long press on the ImageButton is needed, not a short click
        registerForContextMenu(cameraButton)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageViewEdit.setImageBitmap(imageBitmap)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_edit_profile, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.save -> {
                /*
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("FULL_NAME", editText.text.toString())
                editor.putString("NICK_NAME", editText2.text.toString())
                editor.putString("EMAIL_ADD", editText3.text.toString())
                editor.putString("USER_LOCA", editText4.text.toString())
                editor.apply()
                */
                with (sharedPreferences.edit()) {
                    putString("FULL_NAME", editText.text.toString())
                    putString("NICK_NAME", editText2.text.toString())
                    putString("EMAIL_ADD", editText3.text.toString())
                    putString("USER_LOCA", editText4.text.toString())
                    apply()
                }

                val intent = Intent(this, ShowProfileActivity::class.java).also {
                    it.putExtra("group22.lab1.FULL_NAME",editText.text.toString())
                    it.putExtra("group22.lab1.Nickname",editText2.text.toString())
                    it.putExtra("group22.lab1.email",editText3.text.toString())
                    it.putExtra("group22.lab1.Location",editText4.text.toString())
                    it.putExtra("group22.lab1.Image_Profile",imageViewEdit.drawToBitmap())
                }

                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
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
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }
        }

    }


    // Methods to keep the info when we rotate the screen
    // when we rotate the screen of the device, we are destroying the activity, so we want to keep
    // the information that was being displayed in the screen.

    override fun onSaveInstanceState(outState: Bundle) {
        // Info is stored in the Bundle
        super.onSaveInstanceState(outState)
        outState.putString("ed1",editText.text.toString())
        outState.putString("ed2",editText2.text.toString())
        outState.putString("ed3",editText3.text.toString())
        outState.putString("ed4",editText4.text.toString())

    }

    // Now we restore the information
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editText.setText(savedInstanceState.getString("ed1"))
        editText2.setText(savedInstanceState.getString("ed2"))
        editText3.setText(savedInstanceState.getString("ed3"))
        editText4.setText(savedInstanceState.getString("ed4"))
    }

}