package com.example.allocate.detailfragment


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.example.allocate.R
import com.example.allocate.databinding.FragmentDetailBinding
import com.example.allocate.loginfragment.LoginViewModel
import com.example.allocate.model.Hospital

const val LOCATION_CODE = 1

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val transition = TransitionInflater.from(this.activity).inflateTransition(android.R.transition.move)

        sharedElementEnterTransition = ChangeBounds().apply {
            enterTransition = transition
        }
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater,container,false)
        binding.detailVM = ViewModelProviders.of(this).get(DetailViewModel::class.java).also { view ->
            view.hospitalList.observe(this, Observer<List<Hospital>> { list ->
                if(list.isNotEmpty()) {
                    view.setHospitalAdapter(list)
                    setRecyclerViewProperties()
                }
            })
            view.isDataFetched.observe(this, Observer {isFetched ->
                if (isFetched) {
                    binding.addrTxt.visibility = View.VISIBLE
                    binding.waitingTxt.visibility = View.VISIBLE
                }else {
                    binding.addrTxt.visibility = View.GONE
                    binding.waitingTxt.visibility = View.GONE
                }
            })
        }
        binding.lifecycleOwner = this

        return binding.root
    }

    fun setRecyclerViewProperties() {
        binding.hospitalList.setHasFixedSize(true)
        binding.hospitalList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)

    }

    override fun onStart() {
        super.onStart()
        startLocationAction()
    }

    fun startLocationAction() {
        if(isPermissionsGranted()) startUpdating() else getPermission()
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
        binding.detailVM?.getLocation()?.observe(this, Observer {
            binding.detailVM?.setLocation(it.latitude,it.longitude)

        })
    }

    fun getPermission() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_CODE)
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
