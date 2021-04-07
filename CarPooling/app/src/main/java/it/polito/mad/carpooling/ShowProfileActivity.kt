package it.polito.mad.carpooling

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.content.Intent
import android.widget.EditText

class ShowProfileActivity : AppCompatActivity() {
    //private lateinit var textView: TextView
    private lateinit var preferences: SharedPreferences
    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var textView4: TextView
    val REQUEST_CODE = 222

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_profile)

        // iniziatilation of properties
        textView =findViewById<TextView>(R.id.textView)
        textView2 =findViewById<TextView>(R.id.textView2)
        textView3 =findViewById<TextView>(R.id.textView3)
        textView4 =findViewById<TextView>(R.id.textView4)

        preferences = getSharedPreferences("SHARED_PREF",Context.MODE_PRIVATE)
        // Retrieve the information when the app has been closed with Shared Preferences
        val full_name = preferences.getString("FULL_NAME","")
        if (full_name!=""){textView.text = full_name} else{textView.setText("Full Name")}

        val nick_name = preferences.getString("NICK_NAME","")
        if (nick_name!=""){textView2.text = nick_name} else{textView2.setText("Nickname")}

        val email_add = preferences.getString("EMAIL_ADD","")
        if (email_add!=""){textView3.text = email_add} else{textView3.setText("email@address")}

        val user_loca = preferences.getString("USER_LOCA","")
        if (user_loca!=""){textView4.text = user_loca} else{textView4.setText("Location")}


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
                true
                editProfile()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //We save the info in the intent and return to EditProfileActivity
    private fun editProfile(){
        // Inside this method, creat an explicit intent targeting
        // the EditProfileActivity class
        //CAMBIO DE PANTALLA, CUANDO PRESIONO EN SAVE
        val full_name: String = textView.text.toString()
        val nick_name: String = textView2.text.toString()
        val email_add: String = textView3.text.toString()
        val user_loca: String = textView4.text.toString()
        val intent = Intent(this, EditProfileActivity::class.java)
        intent.putExtra("group22.lab1.FULL_NAME",full_name)
        intent.putExtra("group22.lab1.Nickname",nick_name)
        intent.putExtra("group22.lab1.email",email_add)
        intent.putExtra("group22.lab1.Location",user_loca)

        startActivityForResult(intent,REQUEST_CODE)
        //startActivity(intent)
        //finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE){
            if (resultCode == RESULT_OK){
                textView.text=data?.getStringExtra("group22.lab1.FULL_NAME")
                textView2.text=data?.getStringExtra("group22.lab1.Nickname")
                textView3.text=data?.getStringExtra("group22.lab1.email")
                textView4.text=data?.getStringExtra("group22.lab1.Location")

            }
        }
    }
}