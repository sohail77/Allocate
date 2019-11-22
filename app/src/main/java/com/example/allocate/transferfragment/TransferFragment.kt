package com.example.allocate.transferfragment


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater

import com.example.allocate.R
import com.example.allocate.databinding.FragmentTransferBinding
import com.example.allocate.homefragment.ADDR
import com.example.allocate.homefragment.ID
import com.example.allocate.homefragment.NAME
import com.example.allocate.homefragment.PERSON
import kotlinx.android.synthetic.main.fragment_transfer.*


class TransferFragment : Fragment() {

    private lateinit var binding: FragmentTransferBinding
    private var transfer_id = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val transition = TransitionInflater.from(this.activity).inflateTransition(android.R.transition.move)

        sharedElementEnterTransition = ChangeBounds().apply {
            enterTransition = transition
        }

        binding = FragmentTransferBinding.inflate(inflater,container,false)
        binding.transferVM = ViewModelProviders.of(this).get(TransferViewModel::class.java).also {view ->
            view.isLoaded.observe(this, Observer {
                view.setTransferID(transfer_id)
            })

        }

        binding.backBtn.setOnClickListener { findNavController().navigate(R.id.action_transferFragment_to_homeFragment) }
        // Inflate the layout for this fragment
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        transfer_hospital_name.text = arguments?.getString(NAME)
        hospital_addr.text = arguments?.getString(ADDR)
        patient_name.text = "Patient Name: " + arguments?.getString(PERSON)
        transfer_id = arguments?.getInt(ID)!!
    }

    override fun onStart() {
        super.onStart()
        startLocationAction()
    }

    fun startLocationAction() {
        if(isPermissionsGranted()) startUpdating() else Toast.makeText(context,"Please grant location permission",Toast.LENGTH_LONG).show()
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


    fun startUpdating() {
        binding.transferVM?.getLocation()?.observe(this, Observer {
            binding.transferVM?.setLocation(it.latitude,it.longitude)
        })
    }



}
