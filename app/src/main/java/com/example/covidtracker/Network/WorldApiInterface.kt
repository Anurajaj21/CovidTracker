package com.example.covidtracker.Network

import com.example.covidtracker.Models.worldData
import retrofit2.Call
import retrofit2.http.GET

interface WorldApiInterface {

    @GET("all")
    fun WorldResponse(): Call<worldData>

}