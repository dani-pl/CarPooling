package it.polito.mad.carpooling

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap

class ShowProfileActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var textView4: TextView
    val REQUEST_CODE = 222

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_profile)

        // initialization of properties

        imageView = findViewById<ImageView>(R.id.imageView)
        textView = findViewById<TextView>(R.id.textView)
        textView2 = findViewById<TextView>(R.id.textView2)
        textView3 = findViewById<TextView>(R.id.textView3)
        textView4 = findViewById<TextView>(R.id.textView4)

        sharedPreferences = getSharedPreferences("SHARED_PREF",Context.MODE_PRIVATE)
        // Retrieve the information when the app has been closed with Shared Preferences

        textView.text = sharedPreferences.getString("FULL_NAME", textView.text.toString())
        textView2.text = sharedPreferences.getString("NICK_NAME", textView2.text.toString())
        textView3.text = sharedPreferences.getString("EMAIL_ADD", textView3.text.toString())
        textView4.text = sharedPreferences.getString("USER_LOCA", textView4.text.toString())

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_show_profile, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.edit_profile -> {
                editProfile()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Save the info in the intent and return to EditProfileActivity
    private fun editProfile(){
        // Inside this method, create an explicit intent targeting
        // the EditProfileActivity class

        val intent = Intent(this, EditProfileActivity::class.java).also {
            it.putExtra("group22.lab1.Image_Profile", imageView.drawToBitmap())
            it.putExtra("group22.lab1.FULL_NAME", textView.text.toString())
            it.putExtra("group22.lab1.Nickname", textView2.text.toString())
            it.putExtra("group22.lab1.email", textView3.text.toString())
            it.putExtra("group22.lab1.Location", textView4.text.toString())
        }

        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE && resultCode == RESULT_OK){
            imageView.setImageBitmap(data?.getParcelableExtra("group22.lab1.Image_Profile"))
            textView.text=data?.getStringExtra("group22.lab1.FULL_NAME")
            textView2.text=data?.getStringExtra("group22.lab1.Nickname")
            textView3.text=data?.getStringExtra("group22.lab1.email")
            textView4.text=data?.getStringExtra("group22.lab1.Location")
        }
    }
}