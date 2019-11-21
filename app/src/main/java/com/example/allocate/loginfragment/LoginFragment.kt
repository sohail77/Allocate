package com.example.allocate.loginfragment


import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.allocate.R
import com.example.allocate.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        binding.loginVM = ViewModelProviders.of(this).get(LoginViewModel::class.java).also { view ->
            view.token.observe(this@LoginFragment, Observer { token ->
                if(token.isNotEmpty()) {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            })
            view.isLoading.observe(this, Observer { isLoading ->
                if(isLoading) {
                    binding.loginBtn.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }else {
                    binding.loginBtn.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            })
        }

        return binding.root
    }


}
