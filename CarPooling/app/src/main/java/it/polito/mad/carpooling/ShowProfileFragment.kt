package it.polito.mad.carpooling

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShowProfileFragment : Fragment() {

    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var textView4: TextView
    private lateinit var imageView: ImageView
    private lateinit var sharedPreferences: SharedPreferences

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


               sharedPreferences = activity?.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!


              // textView.text = sharedPreferences.getString("FULL_NAME", textView.text.toString())
               //textView2.text = sharedPreferences.getString("NICK_NAME", textView2.text.toString())
               //textView3.text = sharedPreferences.getString("EMAIL_ADD", textView3.text.toString())
               //textView4.text = sharedPreferences.getString("USER_LOCA", textView4.text.toString())



               if(arguments!=null) {
                   imageView.setImageBitmap(arguments?.getParcelable("group22.lab1.Image_Profile"))
                   textView.text = arguments?.getString("group22.lab1.full_name")
                   textView2.text = arguments?.getString("group22.lab1.nickname")
                   textView3.text = arguments?.getString("group22.lab1.email")
                   textView4.text = arguments?.getString("group22.lab1.location")
               }

   }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_show_profile, menu)
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

    private fun editProfile(){

        val bundle = Bundle()
        bundle.putParcelable("group22.lab1.Image_Profile",imageView.drawable.toBitmap())
        bundle.putString("group22.lab1.full_name", textView.text.toString())
        bundle.putString("group22.lab1.nickname", textView2.text.toString())
        bundle.putString("group22.lab1.email", textView3.text.toString())
        bundle.putString("group22.lab1.location", textView4.text.toString())
        findNavController().navigate(R.id.action_showProfileFragment_to_editProfileFragment,bundle)

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