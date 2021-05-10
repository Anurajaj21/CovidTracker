package com.example.covidtracker.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker.Adapters.DistrictAdapter
import com.example.covidtracker.Models.DistrictData
import com.example.covidtracker.R


class StateFragment : Fragment() {

    lateinit var adapter : RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder>
    val list= ArrayList<DistrictData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_state, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.district_rv)
        val district = DistrictData(76689,686897,7667,"Firozabad",7878,"here is some note",66877)
        list.add(district)
        list.add(district)
        list.add(district)
        list.add(district)
        list.add(district)
        list.add(district)
        list.add(district)
        list.add(district)
        list.add(district)
        list.add(district)
        adapter = DistrictAdapter(list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        return view
    }

}