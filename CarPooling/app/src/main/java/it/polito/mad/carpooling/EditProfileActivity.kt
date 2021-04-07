package it.polito.mad.carpooling


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*


class EditProfileActivity : AppCompatActivity() {
    // Declaration of properties

    private lateinit var editText: EditText
    private lateinit var editText2: EditText
    private lateinit var editText3: EditText
    private lateinit var editText4: EditText
    private lateinit var buttonSave: Button
    private lateinit var imageView : ImageView

    private lateinit var sharedPreferences: SharedPreferences
    //https://medium.com/swlh/sharedpreferences-in-android-using-kotlin-6d3bb4ffb71c




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        // iniziatilation of properties
        editText =findViewById<EditText>(R.id.editText)
        editText2 =findViewById<EditText>(R.id.editText2)
        editText3 =findViewById<EditText>(R.id.editText3)
        editText4 =findViewById<EditText>(R.id.editText4)
        buttonSave =findViewById<Button>(R.id.buttonSave)
        imageView = findViewById<ImageView>(R.id.imageView)

        sharedPreferences = getSharedPreferences("SHARED_PREF",Context.MODE_PRIVATE)
        //Here we retrieve the final values of the variables on ShowProfileActivity
        val full_name_init: String? = intent.getStringExtra("group22.lab1.FULL_NAME")
        editText.setText(full_name_init)
        val nick_name_init: String? = intent.getStringExtra("group22.lab1.Nickname")
        editText2.setText(nick_name_init)
        val email_add_init: String? = intent.getStringExtra("group22.lab1.email")
        editText3.setText(email_add_init)
        val user_loca_init: String? = intent.getStringExtra("group22.lab1.Location")
        editText4.setText(user_loca_init)

        //Here we saved the new information the user write
        buttonSave.setOnClickListener {


            // we save the values typed on the editText into strings
            val full_name: String = editText.text.toString()
            val nick_name: String = editText2.text.toString()
            val email_add: String = editText3.text.toString()
            val user_loca: String = editText4.text.toString()

            // we save the values using sharedPreferences to retrieve later
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("FULL_NAME",full_name)
            editor.putString("NICK_NAME",nick_name)
            editor.putString("EMAIL_ADD",email_add)
            editor.putString("USER_LOCA",user_loca)
            editor.apply()
            Toast.makeText(this,"Data saved",Toast.LENGTH_SHORT).show()

            //We save the info in the intent and return to ShowProfileActivity
            val intent = Intent(this, ShowProfileActivity::class.java)
            intent.putExtra("group22.lab1.FULL_NAME",full_name)
            intent.putExtra("group22.lab1.Nickname",nick_name)
            intent.putExtra("group22.lab1.email",email_add)
            intent.putExtra("group22.lab1.Location",user_loca)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }



        val cameraButton = findViewById<ImageButton>(R.id.cameraButton)
        registerForContextMenu(cameraButton)

        //here we launch the camera
        val CAMERA_REQUEST_CODE = 0
        cameraButton.setOnClickListener{              val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(callCameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }


    }

    // Here we save the picture taken by the camera and update the picture on the profile user
    val CAMERA_REQUEST_CODE = 0
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    imageView.setImageBitmap(data?.extras?.get("data") as Bitmap)
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateContextMenu(menu: ContextMenu, v: View,
                                     menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_change_image, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.gallery -> {
                true

            }
            R.id.use_camera -> {
                true

            }
            else -> super.onContextItemSelected(item)
        }
    }

    // methods to keep the info when we rotate the screen
    // when we rotate the screen of the device, we are destroying the activity, so we want to keep
    // the information that was being displayed in the screen.
    override fun onSaveInstanceState(outState: Bundle) { //the info is stored in the Bundle
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