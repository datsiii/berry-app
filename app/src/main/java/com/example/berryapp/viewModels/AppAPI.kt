package com.example.berryapp.viewModels

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AppAPI {
    /*@GET("/api/v1/predict.json/complete")
    fun complete(
        @Query("key") key456356dfg: String,
        @Query("q") test: String,
        @Query("lang") lang: String,
        @Query("limit") limit: Int
    )*/
    //: Call<Answer>
    @POST("/api/auth/signup")
    fun register()
    @POST("/api/auth/login")
    fun login(@Body params: RequestBody?): okhttp3.Call

    companion object {
        const val BASE_URL = "http://orcl.unicorns-group.ru:9008"
    }
}