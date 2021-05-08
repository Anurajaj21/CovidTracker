package com.example.covidtracker.Models

data class DistrictData(
    val active: Int,
    val confirmed: Int,
    val deceased: Int,
    val delta: Delta,
    val district: String,
    val migratedother: Int,
    val notes: String,
    val recovered: Int
)