package com.example.berryapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

data class data_login(
    val name: String,
    val password: String
)
class RegViewModel: ViewModel() {
    private lateinit var retrofit: Retrofit
    private lateinit var api: AppAPI
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    companion object {
        const val BASE_URL = "http://orcl.unicorns-group.ru:9008"
    }

    fun retrofit(): AppAPI{
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(AppAPI::class.java)
        return api
    }

    fun inst(dataLogin: data_login){
        val appAPI = retrofit()
        val jsonObject = JSONObject()
        jsonObject.put("login", "login")
        jsonObject.put("password", "password")
        jsonObject.put("name", "name")
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        val body: RequestBody = requestBody
        appAPI.login(body)?.enqueue(object : okhttp3.Callback{
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.use {
                    if (!response.isSuccessful) {
                        throw IOException("Запрос к серверу не был успешен:" +
                                " ${response.code} ${response.message}")
                    }
                    // пример получения всех заголовков ответа
                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                    // вывод тела ответа
                    println(response.body!!.string())
                }
            }
        })

    }

    // Функция для регистрации с обычной аутентификацией
    fun registerWithBasicAuth(username: String, password: String) {
        coroutineScope.launch {
            try {
                Log.d("kkk", "${inst(data_login(username, password))}")
                inst(data_login(username, password))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}