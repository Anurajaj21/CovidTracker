package com.example.covidtracker.Models.India

data class StateResponse(
        val statewise: ArrayList<Statewise>,
        val tested: ArrayList<Tested>
)