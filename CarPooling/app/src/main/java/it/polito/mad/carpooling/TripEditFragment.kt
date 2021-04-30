package it.polito.mad.carpooling

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import java.text.SimpleDateFormat
import java.util.*


class TripEditFragment : Fragment(R.layout.fragment_trip_edit) {
    private lateinit var departureLocationView: EditText
    private lateinit var arrivalLocationView:EditText
    private lateinit var departureDateView:EditText
    private lateinit var departureTimeView:EditText
    private lateinit var tripDurationView: EditText
    private lateinit var numberOfSeatsView:EditText
    private lateinit var priceView:EditText
    private lateinit var additionalInfoView: EditText
    private lateinit var imageViewCard: ImageView
    private val viewModel: ListViewModel by activityViewModels()
    private var new:Boolean = false

    private val CAMERA_REQUEST_CODE = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val fab= activity?.findViewById<FloatingActionButton>(R.id.fab)
        fab?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position=arguments?.getInt("position")
        viewModel.change_position(position)
        if(viewModel.new.value!!) {
            (activity as MainActivity).supportActionBar?.title = "New Trip"
        }else{
            (activity as MainActivity).supportActionBar?.title = "Edit Trip"
        }

        departureLocationView = view.findViewById<EditText>(R.id.departureLocation_text)
        arrivalLocationView = view.findViewById<EditText>(R.id.arrivalLocation_text)
        departureDateView = view.findViewById<EditText>(R.id.departureDate_text)
        departureTimeView = view.findViewById<EditText>(R.id.departureTime_text)
        tripDurationView = view.findViewById<EditText>(R.id.tripDuration_text)
        numberOfSeatsView = view.findViewById<EditText>(R.id.numberOfSeats_text)
        priceView = view.findViewById<EditText>(R.id.price_text)
        additionalInfoView = view.findViewById<EditText>(R.id.additionalInfo_text)
        imageViewCard = view.findViewById<ImageView>(R.id.imageView)


        // Data Picker
        departureDateView.setOnClickListener { showDatePickerDialog() }
        // Time Picker
        departureTimeView.setOnClickListener {
            val c = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                c.set(Calendar.HOUR_OF_DAY, hour)
                c.set(Calendar.MINUTE, min)
                departureTimeView.setText(SimpleDateFormat("HH:mm").format(c.time))
            }
            TimePickerDialog(context, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }



        if(arguments!=null) {
            departureLocationView.setText(arguments?.getParcelable<Item>("ItemDetails")?.departureLocation.toString())
            departureDateView.setText(arguments?.getParcelable<Item>("ItemDetails")?.departureDate.toString())
            departureTimeView.setText(arguments?.getParcelable<Item>("ItemDetails")?.departureTime.toString())
            tripDurationView.setText(arguments?.getParcelable<Item>("ItemDetails")?.tripDuration.toString())
            priceView.setText(arguments?.getParcelable<Item>("ItemDetails")?.price.toString())
            additionalInfoView.setText(arguments?.getParcelable<Item>("ItemDetails")?.additionalInfo.toString())
            imageViewCard.setImageBitmap(arguments?.getParcelable<Item>("ItemDetails")?.image)
        }


        val cameraButton = view.findViewById<ImageButton>(R.id.cameraButton)
        // Long press on the ImageButton is needed, not a short click
        registerForContextMenu(cameraButton)

    }
    private fun showDatePickerDialog() {
        val dataPicker = DatePickerFragment {day, month, year -> onDateSelected(day, month, year)}
        dataPicker.show(activity?.supportFragmentManager!!, "datePicker")
    }

    fun onDateSelected(day: Int, month: Int, year: Int){
        departureDateView.setText("$day/${month+1}/$year")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater: MenuInflater = inflater
        inflater.inflate(R.menu.menu_edit_profile, menu)
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


    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater? = activity?.menuInflater!!
        inflater?.inflate(R.menu.menu_change_image, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.gallery -> {
                true
            }
            R.id.use_camera -> {
                dispatchTakePictureIntent()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageViewCard.setImageBitmap(imageBitmap)
        }
    }


}


