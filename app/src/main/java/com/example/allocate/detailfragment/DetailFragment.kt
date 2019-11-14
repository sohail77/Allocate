package com.example.allocate.detailfragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

/**
 * A simple [Fragment] subclass.
 */
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
        }
        binding.lifecycleOwner = this

        return binding.root
    }

    fun setRecyclerViewProperties() {
        binding.hospitalList.setHasFixedSize(true)
        binding.hospitalList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)

    }


}
