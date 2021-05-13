package com.example.covidtracker.Models

data class DistrictResponseItem(
    val districtData: List<DistrictData>,
    val state: String,
    val statecode: String
)