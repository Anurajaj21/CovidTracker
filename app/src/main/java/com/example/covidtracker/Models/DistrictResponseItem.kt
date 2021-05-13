package com.example.covidtracker.Models

data class DistrictResponseItem(
    val districtData: ArrayList<DistrictData>,
    val state: String,
    val statecode: String
)