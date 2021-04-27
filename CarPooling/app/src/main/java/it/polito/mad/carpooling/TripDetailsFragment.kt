package it.polito.mad.carpooling

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


class TripDetailsFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position=arguments?.getInt("position")
        viewModel.change_position(position)


        departureLocationView = view.findViewById<TextView>(R.id.departureLocation_text_details)
        arrivalLocationView = view.findViewById<TextView>(R.id.arrivalLocation_text_details)
        departureDateView = view.findViewById<TextView>(R.id.departureDate_text_details)
        departureTimeView = view.findViewById<TextView>(R.id.departureTime_text_details)
        tripDurationView = view.findViewById<TextView>(R.id.tripDuration_text_details)
        numberOfSeatsView = view.findViewById<TextView>(R.id.numberOfSeats_text_details)
        priceView = view.findViewById<TextView>(R.id.price_text_details)
        additionalInfoView = view.findViewById<TextView>(R.id.additionalInfo_text_details)
        imageViewCard = view.findViewById<ImageView>(R.id.imageView_details)


        departureLocationView.text = arguments?.getParcelable<Item>("ItemDetails")?.departureLocation.toString()
        arrivalLocationView.text = arguments?.getParcelable<Item>("ItemDetails")?.arrivalLocation.toString()
        departureDateView.text = arguments?.getParcelable<Item>("ItemDetails")?.departureDate.toString()
        departureTimeView.text = arguments?.getParcelable<Item>("ItemDetails")?.departureTime.toString()
        tripDurationView.text = arguments?.getParcelable<Item>("ItemDetails")?.tripDuration.toString()
        priceView.text = arguments?.getParcelable<Item>("ItemDetails")?.price.toString()
        additionalInfoView.text = arguments?.getParcelable<Item>("ItemDetails")?.additionalInfo.toString()
        imageViewCard.setImageBitmap(arguments?.getParcelable<Item>("ItemDetails")?.image)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater: MenuInflater = inflater
        inflater.inflate(R.menu.menu_show_profile, menu)
        //return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.edit_profile -> {
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
                findNavController().navigate(R.id.action_tripDetailsFragment_to_tripEditFragment,bundle)
            }
        }
        return super.onOptionsItemSelected(item)
    }



}