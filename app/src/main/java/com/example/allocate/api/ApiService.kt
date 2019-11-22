package com.example.allocate.api

import com.example.allocate.model.AuthenticationModel
import com.example.allocate.model.Hospital
import com.example.allocate.model.LocationModel
import com.example.allocate.model.TransferModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


var okHttpClient = OkHttpClient.Builder()
    .readTimeout(20, TimeUnit.MINUTES)
    .connectTimeout(20, TimeUnit.MINUTES)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface ApiService {
    @POST(LOGIN_URL)
    fun login(@Body body: HashMap<String, String>):
            Call<AuthenticationModel>

    @POST(HOSPITAL_URL)
    fun getHospitals(@Header("Content-Type") content: String, @Header("Authorization") auth: String, @Body body: LocationModel):
            Call<List<Hospital>>

    @GET(TRANSFER_CHECK_URL + "/{input}")
    fun getTransferData(@Path("input") input: String, @Header("Authorization") auth: String):
            Call<TransferModel>

    @POST(TRACK + "{input}")
    fun track(@Path("input") input: String, @Header("Content-Type") content: String, @Header("Authorization") auth: String, @Body body: LocationModel):
            Call<Any>
}

object HospitalApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}