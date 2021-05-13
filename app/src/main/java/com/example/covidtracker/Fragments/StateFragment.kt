package com.example.covidtracker.Fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker.Adapters.DistrictAdapter
import com.example.covidtracker.Models.*
import com.example.covidtracker.Network.ApiClient
import com.example.covidtracker.Network.ApiInterface
import com.example.covidtracker.R
import com.example.covidtracker.Utils.LoadingUtils
import kotlinx.android.synthetic.main.fragment_district.*
import kotlinx.android.synthetic.main.fragment_india.*
import kotlinx.android.synthetic.main.fragment_india.in_active_cases
import kotlinx.android.synthetic.main.fragment_state.*
import kotlinx.android.synthetic.main.fragment_world.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eazegraph.lib.models.PieModel
import retrofit2.Response


class StateFragment(private val unit1: Statewise, private  val unit2: Tested) : Fragment() {

    lateinit var adapter : RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder>
    var list = ArrayList<DistrictData>()
    val stateClient = ApiClient("https://api.covid19india.org/v2/")
    val response : MutableLiveData<Response<DistrictResponse>> = MutableLiveData()

    @ExperimentalStdlibApi
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_state, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.district_rv)
        val apiInterface = stateClient.getApiClient()?.create(ApiInterface::class.java)

        GlobalScope.launch(Dispatchers.Main){
            LoadingUtils.showDialog(requireContext(), true)
            response.value = apiInterface?.DistrictResponse()
            LoadingUtils.hideDialog()
            Log.d("district data", response.value?.body().toString())
            setData()
            state_piechart.clearChart()
            setPiechart()
        }
        adapter = DistrictAdapter(list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        return view
    }

    private fun setPiechart(){
        state_piechart.addPieSlice(PieModel("Active", Integer.parseInt(st_active_cases.text.toString()).toFloat(), Color.parseColor("#85601F")))
        state_piechart.addPieSlice(PieModel("Confirmed", Integer.parseInt(st_confirmed_cases.text.toString()).toFloat(), Color.parseColor("#6568EE")))
        state_piechart.addPieSlice(PieModel("Recovered", Integer.parseInt(st_recovered_cases.text.toString()).toFloat(), Color.parseColor("#50A754")))
        state_piechart.addPieSlice(PieModel("Deaths", Integer.parseInt(st_death_cases.text.toString()).toFloat(), Color.parseColor("#F30505")))

        state_piechart.startAnimation()

    }

    @ExperimentalStdlibApi
    @SuppressLint("SetTextI18n")
    private fun setData(){
        st_active_cases.text = unit1.active
        st_confirmed_cases.text = unit1.confirmed
        st_delta_confirmed.text = "+" + unit1.deltaconfirmed
        st_recovered_cases.text = unit1.recovered
        st_delta_recovered.text = "+" + unit1.deltarecovered
        st_death_cases.text = unit1.deaths
        st_delta_deaths.text = "+" + unit1.deltadeaths
        state_note.text = unit1.statenotes
        st_update.text = unit1.lastupdatedtime
        st_tests.text = unit2.totalsamplestested
        st_delta_tests.text = unit2.totalsamplestested

        if(unit1.statenotes == ""){
            state_note.text = "-----"
        }else{
            state_note.text = unit1.statenotes
        }

        response.observe(viewLifecycleOwner, { response->
            if (response.code() == 200){
                for (i in response.body()!!){
//                    Log.d("district", i.toString())
                    if(unit1.state.lowercase().equals(i.state.lowercase())){
                        Log.d("district", i.state)
                        list.addAll(i.districtData)
                        adapter.notifyDataSetChanged()
                    }
                }
//                adapter.notifyDataSetChanged()
            }
        })
    }

}