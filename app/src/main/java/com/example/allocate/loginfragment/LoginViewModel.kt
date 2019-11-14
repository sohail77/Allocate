package com.example.allocate.loginfragment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.allocate.BR
import com.example.allocate.ObservableViewModel
import com.example.allocate.api.*
import com.example.allocate.model.AuthenticationModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class LoginViewModel(application: Application) : ObservableViewModel(application) {

    val token = MutableLiveData<String>()
    val context = application.applicationContext
    private var sharedPreferences: SharedPreferences
    val isLoading = MutableLiveData<Boolean>()

    init {
        sharedPreferences = context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)
        token.value = sharedPreferences.getString(BEARER,"")
        isLoading.value = false
    }

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

    fun login() {
        if(userNameField.isNotEmpty() && passwordField.isNotEmpty()) {

//            val loginData = AuthenticationModel(userNameField,passwordField)
            val params = HashMap<String,String>()
            params["username"] = userNameField
            params["password"] = passwordField
//            val jsonObject = JSONObject(params as Map<*, *>)
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
                val model = response.body()
                val tokenData = model?.token
                token.value = tokenData
                sharedPreferences.edit().putString(BEARER, tokenData).apply()
                isLoading.value = false
            }
        })



//        val request = JsonObjectRequest(Request.Method.POST, LOGIN_URL ,obj,Response.Listener { response ->
//            val strResp= response.toString()
//            val jsonObj= JSONObject(strResp)
//            val tokenData = jsonObj.getString("token")
//            sharedPreferences.edit().putString(BEARER, tokenData).apply()
//
//            token.value = tokenData
//
//        }, Response.ErrorListener { Toast.makeText(context,"Invalid Credentials", Toast.LENGTH_LONG).show() })
//
//        VolleySingleton.getInstance(context).addToRequestQueue(request)

    }
}