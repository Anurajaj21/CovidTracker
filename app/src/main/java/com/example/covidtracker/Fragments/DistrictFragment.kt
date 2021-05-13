package com.example.covidtracker.Fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covidtracker.Models.DistrictData
import com.example.covidtracker.R
import kotlinx.android.synthetic.main.fragment_district.*
import kotlinx.android.synthetic.main.fragment_state.*
import org.eazegraph.lib.models.PieModel


class DistrictFragment(private val unit: DistrictData) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_district, container, false)

        setData()
        setPiechart()

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun setData(){
        ds_active_cases.text = unit.active.toString()
        ds_confirmed_cases.text = unit.confirmed.toString()
        ds_delta_confirmed.text = "+" + unit.delta.confirmed.toString()
        ds_recovered_cases.text = unit.recovered.toString()
        ds_delta_recovered.text = "+" + unit.delta.recovered.toString()
        ds_death_cases.text = unit.deceased.toString()
        ds_delta_deaths.text = "+" + unit.delta.deceased.toString()
        if(unit.notes == ""){
            district_note.text = "-----"
        }else{
            district_note.text = unit.notes
        }

    }

    private fun setPiechart(){
        district_piechart.addPieSlice(PieModel("Active", Integer.parseInt(ds_active_cases.text.toString()).toFloat(), Color.parseColor("#85601F")))
        district_piechart.addPieSlice(PieModel("Confirmed", Integer.parseInt(ds_confirmed_cases.text.toString()).toFloat(), Color.parseColor("#6568EE")))
        district_piechart.addPieSlice(PieModel("Recovered", Integer.parseInt(ds_recovered_cases.text.toString()).toFloat(), Color.parseColor("#50A754")))
        district_piechart.addPieSlice(PieModel("Deaths", Integer.parseInt(ds_death_cases.text.toString()).toFloat(), Color.parseColor("#F30505")))

        district_piechart.startAnimation()

    }
}