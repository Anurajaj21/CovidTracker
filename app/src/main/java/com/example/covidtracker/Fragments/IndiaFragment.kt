package com.example.covidtracker.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker.Adapters.StatesAdapter
import com.example.covidtracker.Models.Statewise
import com.example.covidtracker.R
import kotlinx.android.synthetic.main.fragment_india.*


class IndiaFragment : Fragment() {

    lateinit var adapter: RecyclerView.Adapter<StatesAdapter.StatesViewHolder>
    val list = ArrayList<Statewise>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_india, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.states_rv)
        val state = Statewise("454666", "36553", "2235", "320", "6", "324", "33424", "1323", "242135", "Uttrakhand", "Ut", "")
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        list.add(state)
        adapter = StatesAdapter(list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        return view
    }

}