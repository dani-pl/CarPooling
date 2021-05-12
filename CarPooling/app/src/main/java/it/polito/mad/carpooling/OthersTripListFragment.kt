package it.polito.mad.carpooling

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage

class OthersTripListFragment : Fragment(R.layout.fragment_others_trip_list) {

    lateinit var itemAdapter2: ItemAdapter2
    private lateinit var listCards: MutableList<Item2>
    private val viewModel: ListViewModel by activityViewModels()




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv2 = view.findViewById<RecyclerView>(R.id.rv2)
        rv2.layoutManager = LinearLayoutManager(context)


        val noOthersTripsAvailable = view.findViewById<TextView>(R.id.no_others_trips_available)
        // val searchViewTrip = view.findViewById<SearchView>(R.id.searchView)


        val db = FirebaseFirestore.getInstance()
        db.collection("trips")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if(error!=null) throw error
                if(value!=null){
                    // get Image bitmap
                    val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.reference
                    val trips2 = mutableListOf<Item2>()
                    for (doc in value){
                        var downloadedBitmap: Bitmap
                        var bitmapCar: Bitmap
                        val imageRef = storageRef.child(doc["image"] as String)
                        val carRef = storageRef.child("images/car.jpeg")
                        val ONE_MEGABYTE: Long = 1024 * 1024
                        carRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                            bitmapCar = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                                downloadedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                                trips2.add(
                                    Item2(
                                        doc["identifier"] as String,
                                        doc["departureLocation"] as String,
                                        doc["arrivalLocation"] as String,
                                        doc["departureDate"] as String,
                                        doc["departureTime"] as String,
                                        doc["tripDuration"] as String,
                                        doc["numberOfSeats"] as String,
                                        doc["price"] as String,
                                        doc["additionalInfo"] as String,
                                        downloadedBitmap
                                    )
                                )
                            }.addOnFailureListener {
                                trips2.add(
                                    Item2(
                                        doc["identifier"] as String,
                                        doc["departureLocation"] as String,
                                        doc["arrivalLocation"] as String,
                                        doc["departureDate"] as String,
                                        doc["departureTime"] as String,
                                        doc["tripDuration"] as String,
                                        doc["numberOfSeats"] as String,
                                        doc["price"] as String,
                                        doc["additionalInfo"] as String,
                                        bitmapCar
                                    )
                                )
                            }.addOnCompleteListener {
                                itemAdapter2 = ItemAdapter2(trips2)
                                rv2.adapter = itemAdapter2
                            }
                        }
                    }
                }

            }

        /* ---------------  ------------------ ------------- --------------- ----


        db.collection("users")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if(error!=null) throw error
                if(value!=null){
                    // get Image bitmap
                    val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.reference
                    val trips2 = mutableListOf<Item2>()
                    for (doc in value){
                        var downloadedBitmap2: Bitmap
                        var bitmapCar2: Bitmap
                        val imageRef2 = storageRef.child(doc["image"] as String)
                        val carRef2 = storageRef.child("images/tony.jpg")
                        val ONE_MEGABYTE: Long = 1024 * 1024
                        carRef2.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                            bitmapCar2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            imageRef2.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                                downloadedBitmap2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                trips2.add(
                                    Item2(
                                        doc["fullName"] as String,
                                        downloadedBitmap2
                                    )
                                )
                            }.addOnFailureListener {
                                trips2.add(
                                    Item2(
                                        doc["fullName"] as String,
                                        bitmapCar2
                                    )
                                )
                            }.addOnCompleteListener {
                                itemAdapter2 = ItemAdapter2(trips2)
                                rv2.adapter = itemAdapter2
                            }
                        }
                    }
                }
            }

        //------------------     --------------- ---------------    ---------------
        */
        viewModel.size.observe(viewLifecycleOwner, Observer<Int?> { size->
            if(size!=null) {
                noOthersTripsAvailable.isVisible = size <= 0
            }
        })
        

    }

}
data class Item2(val identifier: String?, val departureLocation:String?, val arrivalLocation:String?,
                val departureDate:String?, val departureTime:String?,
                val tripDuration:String?, val numberOfSeats:String?,
                val price:String?, val additionalInfo:String?,
                val image: Bitmap?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Bitmap::class.java.classLoader)
        //parcel.readString(),
        //parcel.readParcelable(Bitmap::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(identifier)
        parcel.writeString(departureLocation)
        parcel.writeString(arrivalLocation)
        parcel.writeString(departureDate)
        parcel.writeString(departureTime)
        parcel.writeString(tripDuration)
        parcel.writeString(numberOfSeats)
        parcel.writeString(price)
        parcel.writeString(additionalInfo)
        parcel.writeParcelable(image, flags)
        //parcel.writeString(fullName)
        //parcel.writeParcelable(image2, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item2> {
        override fun createFromParcel(parcel: Parcel): Item2 {
            return Item2(parcel)
        }

        override fun newArray(size: Int): Array<Item2?> {
            return arrayOfNulls(size)
        }
    }
}
class ItemAdapter2(val items:MutableList<Item2>): RecyclerView.Adapter<ItemAdapter2.ItemViewHolder>() {


    // the task of a view holder is to remember the smaller parts that my layout is made of.
    // and bind things whenever is requested
    class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val departureLocationText = v.findViewById<TextView>(R.id.departureLocation_text_card)
        val arrivalLocationText = v.findViewById<TextView>(R.id.arrivalLocation_text_card)
        val departureDateText = v.findViewById<TextView>(R.id.departureDate_text_card)
        val departureTimeText = v.findViewById<TextView>(R.id.departureTime_text_card)
        val numberOfSeatsText = v.findViewById<TextView>(R.id.numberOfSeats_text_card)
        val priceText = v.findViewById<TextView>(R.id.price_text_card)
        val imageCard = v.findViewById<ImageView>(R.id.image_card)
        val fullNameText = v.findViewById<TextView>(R.id.fullName_text_card)
        val imageProfile = v.findViewById<ImageView>(R.id.image_profile)


        fun bind(item: Item2) {
            departureLocationText.text = item.departureLocation
            arrivalLocationText.text = item.arrivalLocation
            departureDateText.text = item.departureDate
            departureTimeText.text = item.departureTime
            numberOfSeatsText.text = item.numberOfSeats
            priceText.text = item.price
            imageCard.setImageBitmap(item.image)
            //fullNameText.text = item.fullName
            //imageProfile.setImageBitmap(item.image2)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("identifier", items[position].identifier)
            bundle.putBoolean("new", false)
            Navigation.findNavController(holder.itemView)
                .navigate(R.id.action_othersTripListFragment_to_tripDetailsFragment2, bundle)
        }
        val viewButton = holder.itemView.findViewById<MaterialButton?>(R.id.view_profile_button)
        val editButton = holder.itemView.findViewById<MaterialButton?>(R.id.edit_card_button)
        editButton?.isVisible= false
        viewButton?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("identifier",items[position].identifier)
            bundle.putBoolean("new",false)
            Navigation.findNavController(holder.itemView).navigate(R.id.action_othersTripListFragment_to_showProfileFragment,bundle)
        }
    }
}

class ListViewModel2() : ViewModel() {
    private val _items = MutableLiveData(mutableListOf<Item2>())
    val items: LiveData<MutableList<Item2>> = _items
    private val _new = MutableLiveData(false)
    val new: LiveData<Boolean> = _new
    val _identifier = MutableLiveData(0)
    val identifier: LiveData<Int?> = _identifier
    val _size = MutableLiveData(0)
    val size: LiveData<Int?> = _size
    val _listIdentifiers = MutableLiveData(mutableListOf<Int>())
    val listIdentifiers = _listIdentifiers

    fun addItem(item: Item2) {
        _items.value = items.value.also { it?.add(item) }
    }


    fun isNew(boolean:Boolean){
        _new.value = boolean
    }

    fun editItem(position: Int, item: Item2){
        _items.value = items.value.also{items-> items?.set(position, item) }
    }

    fun change_identifier(identifier: Int?) {
        _identifier.value = identifier
    }

    fun increase_size(){
        _size.value= _size.value?.plus(1)
    }
    fun decrease_size(){
        _size.value= _size.value?.minus(1)
    }
    fun set_size(size: Int){
        _size.value = size
    }

    fun isUnique(identifier: Int): Boolean? {
        return  _listIdentifiers.value?.contains(identifier)
    }

    fun addIdentifier(identifier: Int) {
        _listIdentifiers.value?.add(identifier)
    }
}









