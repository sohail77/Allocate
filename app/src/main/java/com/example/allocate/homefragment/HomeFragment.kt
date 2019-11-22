package com.example.allocate.homefragment


import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.isNotEmpty
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.allocate.R
import com.example.allocate.api.BEARER
import com.example.allocate.api.HospitalApi
import com.example.allocate.api.SHAREDPREF_NAME
import com.example.allocate.detailfragment.LOCATION_CODE
import com.example.allocate.model.TransferModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.search_bottom_sheet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



const val NAME = "name"
const val ADDR = "addr"
const val PERSON = "person"
const val ID = "id"
class HomeFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var transferString: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bottomSheet = BottomSheetBehavior.from(sheet)
        sharedPreferences = context!!.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)

        pickupCard.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                pickupCard to "mainCardTransition")
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment,null,null, extras) }

        transfer_card.setOnClickListener {
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }

        start_btn.setOnClickListener {
                progressBarInSheet.visibility = View.VISIBLE
                makeRequest()
        }



    }

    fun makeRequest() {
        if (search_bar_input.isNotEmpty()) {
            HospitalApi.retrofitService.getTransferData(search_bar_input.editText?.text.toString(),"Bearer " + sharedPreferences.getString(
                BEARER,""))
                .enqueue(object : Callback<TransferModel> {
                    override fun onFailure(call: Call<TransferModel>, t: Throwable) {
                        Toast.makeText(context,"Invalid Transfer ID", Toast.LENGTH_LONG).show()
                        progressBarInSheet.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<TransferModel>,
                        response: Response<TransferModel>
                    ) {
                        if (response.body()!=null){
                            progressBarInSheet.visibility = View.GONE
                            val extras = FragmentNavigatorExtras(
                                start_btn to "transferDetailTransition")
                            findNavController().navigate(R.id.action_homeFragment_to_transferFragment,
                                bundleOf(NAME to response.body()?.toHospital, ADDR to response.body()?.address,
                                    PERSON to response.body()?.patientName,ID to response.body()?.transferId),
                                null, extras)
                        }else {
                            Toast.makeText(context,"Invalid Transfer ID", Toast.LENGTH_LONG).show()
                            progressBarInSheet.visibility = View.GONE
                        }
                    }
                })
        }else{
            Toast.makeText(context,"Enter the Transfer ID", Toast.LENGTH_LONG).show()
            progressBarInSheet.visibility = View.GONE

        }
    }

    override fun onStart() {
        super.onStart()
        startLocationAction()
    }

    fun startLocationAction() {
        if(!isPermissionsGranted()) getPermission()
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED


    fun getPermission() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_CODE -> startLocationAction()
        }
    }

}
