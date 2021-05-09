package it.polito.mad.carpooling

import  android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Insets.add
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import androidx.viewbinding.ViewBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.lang.reflect.Array.get

class MainActivity : AppCompatActivity(), DrawerLayout.DrawerListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: ListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val imageViewDrawer = navView.getHeaderView(0).findViewById<ImageView>(R.id.imageView_drawer)
        val nameDrawer = navView.getHeaderView(0).findViewById<TextView>(R.id.name_drawer)

            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document("user1")
                .addSnapshotListener { value, error ->
                    if(error!=null) throw error
                    if (value != null) {
                        nameDrawer.text = value["nickname"].toString()
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
                                    imageViewDrawer.setImageBitmap(
                                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                    )
                                }
                                .addOnCanceledListener {
                                    imageViewDrawer.setImageBitmap(bitmapCar)
                                }
                        }
                    }
                }


        val fab: FloatingActionButton = findViewById(R.id.fab)

        // how many trips were stored
        var count = 0
        db.collection("trips")
            .get()
            .addOnSuccessListener { value ->
                //if(error!=null) throw error
                if (value != null) {
                    for(document in value) {
                        count=count.plus(1)
                        viewModel.set_size(value.size())
                    }
                    viewModel.set_size(count)
                }
            }



        fab.setOnClickListener {
            navController.navigate(R.id.action_tripListFragment_to_tripEditFragment)
            fab.hide()
            viewModel.isNew(true)
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(setOf(R.id.tripListFragment, R.id.showProfileFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onDrawerStateChanged(newState: Int) {
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
    }

    override fun onDrawerClosed(drawerView: View) {
    }

    override fun onDrawerOpened(drawerView: View) {
        val imageViewDrawer = drawerView.findViewById<ImageView>(R.id.imageView_drawer)
        val nameDrawer = drawerView.findViewById<TextView>(R.id.name_drawer)

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document("user1")
            .get()
            .addOnSuccessListener { value ->
                if (value != null) {
                    nameDrawer.text = value["fullName"].toString()
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
                                imageViewDrawer.setImageBitmap(
                                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                )
                            }
                            .addOnCanceledListener {
                                imageViewDrawer.setImageBitmap(bitmapCar)
                            }
                    }
                }
            }
    }



}