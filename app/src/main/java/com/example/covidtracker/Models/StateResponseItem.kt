package com.example.covidtracker.Models

data class StateResponseItem(
    val districtData: List<DistrictData>,
    val state: String,
    val statecode: String
)