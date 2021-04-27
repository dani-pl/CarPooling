package it.polito.mad.carpooling

import android.graphics.Bitmap

interface Communicator{
    fun onChange(departureLocation:String, arrivalLocation:String,
                 departureDate:String, departureTime:String,
                 tripDuration:String, numberOfSeats:String,
                 price:String, additionalInfo:String,
                 image: Bitmap
    )
}