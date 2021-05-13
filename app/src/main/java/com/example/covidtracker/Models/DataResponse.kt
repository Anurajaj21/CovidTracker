package com.example.covidtracker.Models

data class DataResponse(
    val cases_time_series: ArrayList<CasesTimeSery>,
    val statewise: ArrayList<Statewise>,
    val tested: ArrayList<Tested>
)