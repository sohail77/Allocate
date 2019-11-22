package com.example.allocate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.allocate.loginfragment.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            val fragment = LoginFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.my_Nav, fragment)
                .commit()
        }

    }
}
