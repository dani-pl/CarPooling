package it.polito.mad.carpooling

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TripListFragment : Fragment(R.layout.fragment_trip_list) {
    lateinit var itemAdapter: ItemAdapter
    private lateinit var listCards: MutableList<Item>
    private val viewModel: ListViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        rv.layoutManager = LinearLayoutManager(context)
        listCards= mutableListOf<Item>()
        val noDataAvailable = view.findViewById<TextView>(R.id.no_data_available)
        val fab= activity?.findViewById<FloatingActionButton>(R.id.fab)
        fab?.show()
        viewModel.items.observe(viewLifecycleOwner,Observer<MutableList<Item>> { items->
            if(items.isNotEmpty()){
                noDataAvailable.isVisible = false
            }
            for (item in items) {
                listCards.add(item)
            }
        })
        itemAdapter = ItemAdapter(listCards)
        rv.adapter = itemAdapter


    }


}

data class Item(val departureLocation:String?, val arrivalLocation:String?,
                val departureDate:String?, val departureTime:String?,
                val tripDuration:String?, val numberOfSeats:String?,
                val price:String?, val additionalInfo:String?,
                val image:Bitmap?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Bitmap::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(departureLocation)
        parcel.writeString(arrivalLocation)
        parcel.writeString(departureDate)
        parcel.writeString(departureTime)
        parcel.writeString(tripDuration)
        parcel.writeString(numberOfSeats)
        parcel.writeString(price)
        parcel.writeString(additionalInfo)
        parcel.writeParcelable(image, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}


class ItemAdapter(val items:MutableList<Item>): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

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

        fun bind(item: Item) {
            departureLocationText.text = item.departureLocation
            arrivalLocationText.text = item.arrivalLocation
            departureDateText.text = item.departureDate
            departureTimeText.text = item.departureTime
            numberOfSeatsText.text = item.numberOfSeats
            priceText.text = item.price
            imageCard.setImageBitmap(item.image)
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

        holder.itemView.setOnClickListener{
            val bundle = Bundle()
            bundle.putParcelable("ItemDetails",items[position])
            bundle.putInt("position",position)
            Navigation.findNavController(holder.itemView).navigate(R.id.action_tripListFragment_to_tripDetailsFragment,bundle)
        }
        val editButton = holder.itemView.findViewById<MaterialButton?>(R.id.edit_card_button)

        editButton?.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("ItemDetails",items[position])
            bundle.putInt("position",position)
            Navigation.findNavController(holder.itemView).navigate(R.id.action_tripListFragment_to_tripEditFragment,bundle)
        }
    }
    fun add(item:Item){
        items.add(item)
        this.notifyItemInserted(items.size - 1)
    }
    fun edit(position: Int, item:Item){
        items[position]=item
    }
}

class ListViewModel() : ViewModel() {
    private val _items = MutableLiveData(mutableListOf<Item>())
    val items: LiveData<MutableList<Item>> = _items
    private val _new = MutableLiveData(false)
    val new: LiveData<Boolean> = _new
    val _position = MutableLiveData<Int?>()
    val position: LiveData<Int?> = _position
/*
    val _image_drawer= MutableLiveData<Bitmap?>()
    val image_drawer: LiveData<Bitmap?> = _image_drawer

 */

    fun addItem(item: Item) {
        _items.value = items.value.also { it?.add(item) }
    }


    fun isNew(boolean:Boolean){
        _new.value = boolean
    }

    fun editItem(position: Int, item: Item){
        _items.value = items.value.also{items-> items?.set(position, item) }
    }

    fun change_position(position: Int?) {
        _position.value = position
    }
    /*
    fun change_image_drawer(image: Bitmap?){
            _image_drawer.value = image
    }

     */

    //fun removeItem(item: Item) { item.add() }
}