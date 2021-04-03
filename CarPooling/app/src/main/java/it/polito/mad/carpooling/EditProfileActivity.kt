package it.polito.mad.carpooling

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

class EditProfileActivity : AppCompatActivity() {
    // Declaration of properties
    private lateinit var editText: EditText
    private lateinit var editText2: EditText
    private lateinit var editText3: EditText
    private lateinit var editText4: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // iniziatilation of properties
        editText =findViewById<EditText>(R.id.editText)
        editText2 =findViewById<EditText>(R.id.editText2)
        editText3 =findViewById<EditText>(R.id.editText3)
        editText4 =findViewById<EditText>(R.id.editText4)

        val cameraButton = findViewById<ImageButton>(R.id.cameraButton)
        registerForContextMenu(cameraButton)
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