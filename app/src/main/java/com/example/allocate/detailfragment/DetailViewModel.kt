package com.example.allocate.detailfragment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.allocate.ObservableViewModel
import com.example.allocate.adapter.HospitalListAdapter
import com.example.allocate.api.*
import com.example.allocate.location.LocationListener
import com.example.allocate.model.Hospital
import com.example.allocate.model.LocationModel
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class DetailViewModel (application: Application) : ObservableViewModel(application) {

    private var sharedPreferences: SharedPreferences
    val context = application.applicationContext
    val hospitalList = MutableLiveData<List<Hospital>>()
    val hospitalName = MutableLiveData<String>()
    val waitingtime = MutableLiveData<String>()
    val isDataFetched = MutableLiveData<Boolean>()

    private var isRequestMade = false
    var adapter: HospitalListAdapter
    var locationUpdates = LocationListener(application)
    private lateinit var location: LocationModel

    init {
        sharedPreferences = context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)
        adapter = HospitalListAdapter(context)
        isDataFetched.value = false
    }

    fun getLocation () = locationUpdates

    fun setLocation(lat: Double, lon: Double) {
        location = LocationModel(lat,lon)
        if(!isRequestMade) {
            makeRequest()
            isRequestMade = true
        }

    }

    fun makeRequest() {
        HospitalApi.retrofitService.getHospitals("application/json","Bearer " + sharedPreferences.getString(BEARER,""),location)
            .enqueue(object :Callback<List<Hospital>>{
            override fun onFailure(call: Call<List<Hospital>>, t: Throwable) {
                isDataFetched.value = true
                Toast.makeText(context,"Invalid Credentials", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<Hospital>>,
                response: retrofit2.Response<List<Hospital>>
            ) {
               val list = response.body()
                hospitalList.value = list
                hospitalName.value = list?.get(0)?.name
                waitingtime.value = list?.get(0)?.waitingTime.toString()
                isDataFetched.value = true
               Log.e("Hello",list.toString())
            }
        })

    }

    fun getHospitalAdapter() = adapter

    fun setHospitalAdapter(list: List<Hospital>) {
        adapter.setUpList(list)
        adapter.notifyDataSetChanged()
    }


}