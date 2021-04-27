package it.polito.mad.carpooling

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Observer


class TripEditFragment : Fragment(R.layout.fragment_trip_edit) {
    private lateinit var departureLocationView: TextView
    private lateinit var arrivalLocationView:TextView
    private lateinit var departureDateView:TextView
    private lateinit var departureTimeView:TextView
    private lateinit var tripDurationView: TextView
    private lateinit var numberOfSeatsView:TextView
    private lateinit var priceView:TextView
    private lateinit var additionalInfoView: TextView
    private lateinit var imageViewCard: ImageView
    private val viewModel: ListViewModel by activityViewModels()
    //private lateinit var viewModel: ListViewModel
    private var new:Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onResume() {
        super.onResume()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProvider(requireActivity()).get(it.polito.mad.carpooling.ListViewModel::class.java)
        val position=arguments?.getInt("position")
        viewModel.change_position(position)

        departureLocationView = view.findViewById<EditText>(R.id.departureLocation_text)
        arrivalLocationView = view.findViewById<EditText>(R.id.arrivalLocation_text)
        departureDateView = view.findViewById<EditText>(R.id.departureDate_text)
        departureTimeView = view.findViewById<EditText>(R.id.departureTime_text)
        tripDurationView = view.findViewById<EditText>(R.id.tripDuration_text)
        numberOfSeatsView = view.findViewById<EditText>(R.id.numberOfSeats_text)
        priceView = view.findViewById<EditText>(R.id.price_text)
        additionalInfoView = view.findViewById<EditText>(R.id.additionalInfo_text)
        imageViewCard = view.findViewById<ImageView>(R.id.imageView)


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater: MenuInflater = inflater
        inflater.inflate(R.menu.menu_edit_profile, menu)
        //return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.save -> {
                if(viewModel.new.value!!){
                    viewModel.isNew(false)
                    viewModel.addItem(
                            Item(
                                    departureLocationView.text.toString(),
                                    arrivalLocationView.text.toString(),
                                    departureDateView.text.toString(),
                                    departureTimeView.text.toString(),
                                    tripDurationView.text.toString(),
                                    numberOfSeatsView.text.toString(),
                                    priceView.text.toString(),
                                    additionalInfoView.text.toString(),
                                    imageViewCard.drawable.toBitmap()))
                }else {
                    val a = viewModel.position.value
                    viewModel.editItem(viewModel.position.value!!,
                            Item(
                            departureLocationView.text.toString(),
                            arrivalLocationView.text.toString(),
                            departureDateView.text.toString(),
                            departureTimeView.text.toString(),
                            tripDurationView.text.toString(),
                            numberOfSeatsView.text.toString(),
                            priceView.text.toString(),
                            additionalInfoView.text.toString(),
                            imageViewCard.drawable.toBitmap())
                    )
                }

                findNavController().navigate(R.id.action_tripEditFragment_to_tripListFragment)
            }

        }
        return super.onOptionsItemSelected(item)
    }
}


