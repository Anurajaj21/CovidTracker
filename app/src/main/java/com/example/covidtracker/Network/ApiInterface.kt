package com.example.covidtracker.Network

import com.example.covidtracker.Models.DataResponse
import com.example.covidtracker.Models.DistrictResponse
import com.example.covidtracker.Models.countryData
import com.example.covidtracker.Models.worldData
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

//    companion object {
//        operator fun invoke(): ApiInterface {
//            return  Retrofit.Builder().baseUrl("https://corona.lmao.ninja/v2/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//                    .create(ApiInterface::class.java)
//        }
//    }

    @GET("all")
    suspend fun WorldResponse(): Response<worldData>

    @GET("countries")
    suspend fun CountryResponse(): Response<countryData>

    @GET("data.json")
   suspend fun StateResponse(): Response<DataResponse>

    @GET("state_district_wise.json")
    suspend fun DistrictResponse(): Response<DistrictResponse>

}