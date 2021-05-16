package com.example.covidtracker.Fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.covidtracker.Models.District.DistrictData
import com.example.covidtracker.R
import com.example.covidtracker.Utils.InternetCheck
import kotlinx.android.synthetic.main.fragment_country.*
import kotlinx.android.synthetic.main.fragment_district.*
import kotlinx.android.synthetic.main.fragment_india.*
import kotlinx.android.synthetic.main.fragment_state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eazegraph.lib.models.PieModel


class DistrictFragment(private val unit: DistrictData) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_district, container, false)

        val back = view.findViewById<ImageView>(R.id.ds_back)
        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.district_refresh)
        val retry = view.findViewById<Button>(R.id.ds_retry)

        back.setOnClickListener {
            activity?.onBackPressed()
        }

        fetchAllData()

        retry.setOnClickListener {
            InternetCheck{
                if (it){
                    ds_no_internet.visibility = View.GONE
                    district.visibility = View.VISIBLE
                    fetchAllData()
                }else{
                    Toast.makeText(requireContext(),"No internet", Toast.LENGTH_SHORT).show()
                }
            }
        }

        refresh.setOnRefreshListener {
            InternetCheck{
                if (it){
                    ds_no_internet.visibility = View.GONE
                    district.visibility = View.VISIBLE
                    fetchAllData()
                }else{
                    district.visibility = View.GONE
                    ds_no_internet.visibility = View.VISIBLE
                }
            }
            refresh.isRefreshing = false
        }

        return view
    }

    private fun fetchAllData(){
        GlobalScope.launch(Dispatchers.Main){
            setData()
            district_piechart.clearChart()
            setPiechart()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(){
        ds_name.text = unit.district
        ds_active_cases.text = unit.active.toString()
        ds_confirmed_cases.text = unit.confirmed.toString()
        ds_delta_confirmed.text = "+" + unit.delta.confirmed.toString()
        ds_recovered_cases.text = unit.recovered.toString()
        ds_delta_recovered.text = "+" + unit.delta.recovered.toString()
        ds_death_cases.text = unit.deceased.toString()
        ds_delta_deaths.text = "+" + unit.delta.deceased.toString()
        if(unit.notes == ""){
            district_note.text = "- - - - - - - - - - -"
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