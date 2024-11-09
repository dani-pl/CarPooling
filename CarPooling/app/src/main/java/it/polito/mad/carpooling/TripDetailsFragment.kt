package it.polito.mad.carpooling

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class TripDetailsFragment : Fragment(R.layout.fragment_trip_details) {
    private lateinit var departureLocationView: TextView
    private lateinit var arrivalLocationView: TextView
    private lateinit var departureDateView: TextView
    private lateinit var departureTimeView: TextView
    private lateinit var tripDurationView: TextView
    private lateinit var numberOfSeatsView: TextView
    private lateinit var priceView: TextView
    private lateinit var additionalInfoView: TextView
    private lateinit var imageViewCard: ImageView
    private val viewModel: ListViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val fab= activity?.findViewById<FloatingActionButton>(R.id.fab)
        fab?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val identifier=arguments?.getString("identifier")
        if(identifier!=null) {
            viewModel.change_identifier(identifier.toInt())
        }
        val new = arguments?.getBoolean("new")
        if (new!=null) {
            if (!new) {
                viewModel.isNew(false)
            }
        }

        departureLocationView = view.findViewById<TextView>(R.id.departureLocation_text_details)
        arrivalLocationView = view.findViewById<TextView>(R.id.arrivalLocation_text_details)
        departureDateView = view.findViewById<TextView>(R.id.departureDate_text_details)
        departureTimeView = view.findViewById<TextView>(R.id.departureTime_text_details)
        tripDurationView = view.findViewById<TextView>(R.id.tripDuration_text_details)
        numberOfSeatsView = view.findViewById<TextView>(R.id.numberOfSeats_text_details)
        priceView = view.findViewById<TextView>(R.id.price_text_details)
        additionalInfoView = view.findViewById<TextView>(R.id.additionalInfo_text_details)
        imageViewCard = view.findViewById<ImageView>(R.id.imageView_details)

        val db = FirebaseFirestore.getInstance()
        db.collection("trips").document("trip${viewModel.identifier.value}")
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null) {
                    departureLocationView.text = doc["departureLocation"] as String
                    arrivalLocationView.text = doc["arrivalLocation"] as String
                    departureDateView.text = doc["departureDate"] as String
                    departureTimeView.text = doc["departureTime"] as String
                    tripDurationView.text = doc["tripDuration"] as String
                    numberOfSeatsView.text = doc["numberOfSeats"] as String
                    priceView.text = doc["price"] as String
                    additionalInfoView.text = doc["additionalInfo"] as String

                    val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.reference
                    val imageRef = storageRef.child(doc["image"] as String)
                    val ONE_MEGABYTE: Long = 1024 * 1024
                    var bitmapCar: Bitmap
                    val carRef = storageRef.child("images/car.jpeg")
                    carRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                        bitmapCar = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                            imageViewCard.setImageBitmap(
                                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            )
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
                /*
                val item =   Item(
                        departureLocationView.text.toString(),
                        arrivalLocationView.text.toString(),
                        departureDateView.text.toString(),
                        departureTimeView.text.toString(),
                        tripDurationView.text.toString(),
                        numberOfSeatsView.text.toString(),
                        priceView.text.toString(),
                        additionalInfoView.text.toString(),
                        imageViewCard.drawable.toBitmap())
                viewModel.editItem(viewModel.position.value!!,item)

                val bundle = Bundle()
                bundle.putParcelable("ItemDetails",item)
                bundle.putInt("position",viewModel.position.value!!)

                 */
                findNavController().navigate(R.id.action_tripDetailsFragment_to_tripEditFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }



}