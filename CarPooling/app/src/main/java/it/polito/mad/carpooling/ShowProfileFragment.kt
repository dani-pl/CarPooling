package it.polito.mad.carpooling

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

class ShowProfileFragment : Fragment() {

    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var textView4: TextView
    private lateinit var imageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val fab= activity?.findViewById<FloatingActionButton>(R.id.fab)
        fab?.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_profile, container, false)
    }


   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById<ImageView>(R.id.imageView_show_profile)
        textView = view.findViewById<TextView>(R.id.textView1_show_profile)
        textView2 = view.findViewById<TextView>(R.id.textView2_show_profile)
        textView3 = view.findViewById<TextView>(R.id.textView3_show_profile)
        textView4 = view.findViewById<TextView>(R.id.textView4_show_profile)
       val db = FirebaseFirestore.getInstance()
       db.collection("users")
           .document("user1")
           .get()
           .addOnSuccessListener { value ->
               if (value != null) {
                   textView.setText(value["fullName"].toString())
                   textView2.setText(value["nickname"].toString())
                   textView3.setText(value["email"].toString())
                   textView4.setText(value["location"].toString())

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
                           imageView.setImageBitmap(
                               BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                           )
                           }
                           .addOnCanceledListener {
                               imageView.setImageBitmap(bitmapCar)
                           }
                   }
               }
           }

   }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_show_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.edit_profile -> {
                findNavController().navigate(R.id.action_showProfileFragment_to_editProfileFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(!outState.isEmpty) {
            outState.putParcelable("im", imageView.drawable.toBitmap())
            outState.putString("t1", textView.text.toString())
            outState.putString("t2", textView2.text.toString())
            outState.putString("t3", textView3.text.toString())
            outState.putString("t4", textView4.text.toString())
        }
    }

    /*
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        imageView.setImageBitmap(savedInstanceState?.getParcelable("im"))
        textView.text = savedInstanceState?.getString("t1")
        textView2.text = savedInstanceState?.getString("t2")
        textView3.text = savedInstanceState?.getString("t3")
        textView4.text = savedInstanceState?.getString("t4")
    }

    */

}