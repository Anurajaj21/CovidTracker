package com.example.covidtracker.Network

import com.example.covidtracker.Models.India.StateResponse
import com.example.covidtracker.Models.District.DistrictResponse
import com.example.covidtracker.Models.World.countryData
import com.example.covidtracker.Models.World.worldData
import retrofit2.Response
import retrofit2.http.GET

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
   suspend fun StateResponse(): Response<StateResponse>

    @GET("state_district_wise.json")
    suspend fun DistrictResponse(): Response<DistrictResponse>

}