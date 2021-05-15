package com.example.covidtracker.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.RenderMode
import com.example.covidtracker.Models.District.DistrictResponse
import com.example.covidtracker.Network.ApiClient
import com.example.covidtracker.Network.ApiInterface
import com.example.covidtracker.R
import com.example.covidtracker.Utils.InternetCheck
import com.example.covidtracker.Utils.LoadingUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eazegraph.lib.models.PieModel
import retrofit2.Response
import java.util.*


class HomeFragment : Fragment() {

    private val homeClient = ApiClient("https://api.covid19india.org/v2/")
    private val response: MutableLiveData<Response<DistrictResponse>> = MutableLiveData()
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private var state = String()
    private var district = String()

    @ExperimentalStdlibApi
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.home_refesh)
        val retry = view.findViewById<Button>(R.id.ho_retry)

        InternetCheck{
            if(!it){
                home.visibility = View.GONE
                ho_no_internet.visibility = View.VISIBLE
            }
        }

        fetchAllData(view)

        retry.setOnClickListener {
            InternetCheck{
                if (it){
                    ho_no_internet.visibility = View.GONE
                    home.visibility = View.VISIBLE
                    fetchAllData(view)
                }else{
                    Toast.makeText(requireContext(),"No internet", Toast.LENGTH_SHORT).show()
                }
            }
        }

        refresh.setOnRefreshListener {
            InternetCheck{
                if (it){
                    ho_no_internet.visibility = View.GONE
                    home.visibility = View.VISIBLE
                    fetchAllData(view)
                }else{
                    home.visibility = View.GONE
                    ho_no_internet.visibility = View.VISIBLE
                }
            }
            refresh.isRefreshing = false
        }

        return view
    }


    @ExperimentalStdlibApi
    @RequiresApi(Build.VERSION_CODES.N)
    private fun fetchAllData(view: View) {

        val apiInterface = homeClient.getApiClient()?.create(ApiInterface::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        getlocation()
        GlobalScope.launch ( Dispatchers.Main ){
            LoadingUtils.showDialog(requireActivity(),true)

            response.value = apiInterface?.DistrictResponse()
            val animationView = view.findViewById<LottieAnimationView>(R.id.lottieAnimationView)
            animationView.setRenderMode(RenderMode.SOFTWARE)


            LoadingUtils.hideDialog()

            setData()
            ho_state_piechart.clearChart()
            setPiechart()
        }
    }
    private fun getlocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 44)

            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            val location = it.result
            if(location != null){
                val geoCoder = Geocoder(requireActivity(), Locale.getDefault())
                val address = geoCoder.getFromLocation(
                    location.latitude, location.longitude, 1
                )
                state = address[0].adminArea
                district = address[0].subAdminArea
                Log.i("locality", state)
                ho_name.text = district
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @ExperimentalStdlibApi
    private fun setData() {

        response.observe(viewLifecycleOwner, {
            if (it.code() == 200){
                for (i in it.body()!!){
                    if(i.state == state){
                        Log.d("area", i.state)
                        for(j in i.districtData){
                            if (j.district.lowercase() == district.lowercase()){
                                ho_active_cases.text = j.active.toString()
                                ho_confirmed_cases.text = j.confirmed.toString()
                                ho_delta_confirmed.text = "+" + j.delta.confirmed
                                ho_recovered_cases.text = j.recovered.toString()
                                ho_delta_recovered.text = "=" + j.delta.recovered
                                ho_death_cases.text = j.deceased.toString()
                                ho_delta_deaths.text = "+" + j.delta.deceased
                                ho_district_note.text = j.notes
                            }
                        }
                    }
                }
            }
        })

    }

    private fun setPiechart(){
        ho_state_piechart.addPieSlice(PieModel("Active", Integer.parseInt(ho_active_cases.text.toString()).toFloat(), Color.parseColor("#85601F")))
        ho_state_piechart.addPieSlice(PieModel("Confirmed", Integer.parseInt(ho_confirmed_cases.text.toString()).toFloat(), Color.parseColor("#6568EE")))
        ho_state_piechart.addPieSlice(PieModel("Recovered", Integer.parseInt(ho_recovered_cases.text.toString()).toFloat(), Color.parseColor("#50A754")))
        ho_state_piechart.addPieSlice(PieModel("Deaths", Integer.parseInt(ho_death_cases.text.toString()).toFloat(), Color.parseColor("#F30505")))

        ho_state_piechart.startAnimation()

    }
}