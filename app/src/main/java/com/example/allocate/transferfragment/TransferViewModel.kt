package com.example.allocate.transferfragment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.allocate.ObservableViewModel
import com.example.allocate.api.BEARER
import com.example.allocate.api.HospitalApi
import com.example.allocate.api.SHAREDPREF_NAME
import com.example.allocate.location.LocationListener
import com.example.allocate.model.LocationModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransferViewModel(application: Application) : ObservableViewModel(application) {


    private var sharedPreferences: SharedPreferences
    val context = application.applicationContext
    var locationUpdates = LocationListener(application)
    private lateinit var location: LocationModel
    private var transferId: String = ""
    var isLoaded = MutableLiveData<Boolean>()

    init {
        sharedPreferences = context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)
        isLoaded.value = true
    }

    fun getLocation() = locationUpdates

    fun setLocation(lat: Double, lon: Double) {
        location = LocationModel(lat, lon)
        makeRequest()
    }

    fun setTransferID(id: Int) {
        transferId = id.toString()
    }

    fun makeRequest() {
        HospitalApi.retrofitService.track(
            transferId, "application/json", "Bearer " + sharedPreferences.getString(
                BEARER, ""
            ), location
        ).enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {

            }
        })
    }
}