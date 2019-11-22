package com.example.allocate.loginfragment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.example.allocate.BR
import com.example.allocate.ObservableViewModel
import com.example.allocate.api.*
import com.example.allocate.model.AuthenticationModel
import retrofit2.Call
import retrofit2.Callback

class LoginViewModel(application: Application) : ObservableViewModel(application) {

    val token = MutableLiveData<String>()
    val context = application.applicationContext
    private var sharedPreferences: SharedPreferences
    val isLoading = MutableLiveData<Boolean>()


    var userNameField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.userNameField)
        }

    var passwordField: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.passwordField)
        }



    init {
        sharedPreferences = context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)
        token.value = sharedPreferences.getString(BEARER,"")
        isLoading.value = false
    }



    fun login() {
        if(userNameField.isNotEmpty() && passwordField.isNotEmpty()) {

            val params = HashMap<String,String>()
            params["username"] = userNameField
            params["password"] = passwordField
            makeRequest(params)
        }else {
            Toast.makeText(context,"Fields cannot be empty", Toast.LENGTH_LONG).show()
        }

    }

    fun makeRequest(obj: HashMap<String,String>) {
        isLoading.value = true

        HospitalApi.retrofitService.login(obj).enqueue(object : Callback<AuthenticationModel> {
            override fun onFailure(call: Call<AuthenticationModel>, t: Throwable) {
                Toast.makeText(context,"Invalid Credentials", Toast.LENGTH_LONG).show()
                isLoading.value = false

            }

            override fun onResponse(call: Call<AuthenticationModel>, response: retrofit2.Response<AuthenticationModel>) {
                if (response.code() == 401 || response.code() == 402) {
                    Toast.makeText(context,"Invalid Credentials", Toast.LENGTH_LONG).show()
                    isLoading.value = false
                }else {
                    val model = response.body()
                    val tokenData = model?.token
                    token.value = tokenData
                    sharedPreferences.edit().putString(BEARER, tokenData).apply()
                    isLoading.value = false
                }
            }
        })

    }
}